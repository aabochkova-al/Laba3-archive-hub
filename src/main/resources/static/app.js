const API_BASE = '/api';

// DOM элементы
const fileInput = document.getElementById('fileInput');
const uploadBtn = document.getElementById('uploadBtn');
const uploadResult = document.getElementById('uploadResult');
const reportId = document.getElementById('reportId');
const reportFormat = document.getElementById('reportFormat');
const generateBtn = document.getElementById('generateBtn');
const reportResult = document.getElementById('reportResult');
const refreshBtn = document.getElementById('refreshBtn');
const missionsList = document.getElementById('missionsList');

// Загрузка миссии
uploadBtn.addEventListener('click', uploadMission);
generateBtn.addEventListener('click', generateReport);
refreshBtn.addEventListener('click', loadMissions);

// Клик по области загрузки для выбора файла
document.getElementById('uploadArea').addEventListener('click', (e) => {
    if (e.target !== fileInput && e.target !== uploadBtn) {
        fileInput.click();
    }
});
fileInput.addEventListener('click', (e) => e.stopPropagation());

async function uploadMission() {
    if (!fileInput.files[0]) {
        showUploadResult('Выберите файл для загрузки', 'error');
        return;
    }
    
    const formData = new FormData();
    formData.append('file', fileInput.files[0]);
    
    showUploadResult('Загрузка...', 'loading');
    
    try {
        const response = await fetch(`${API_BASE}/missions/upload`, {
            method: 'POST',
            body: formData
        });
        
        const text = await response.text(); // сначала читаем как текст
        
        if (response.ok) {
            let data;
            try {
                data = JSON.parse(text);
            } catch (e) {
                data = { missionId: 'unknown', id: 'unknown' };
            }
            
            showUploadResult(`Миссия "${data.missionId || '—'}" успешно импортирована (ID: ${data.id})`, 'success');
            fileInput.value = '';
            loadMissions();
        } else {
            showUploadResult(`Ошибка: ${text}`, 'error');
        }
    } catch (error) {
        showUploadResult(`Ошибка сети: ${error.message}`, 'error');
    }
}

function showUploadResult(message, type) {
    uploadResult.textContent = message;
    uploadResult.className = 'result-message';
    if (type !== 'loading') {
        uploadResult.classList.add(type);
    }
    if (type === 'loading') {
        uploadResult.style.color = '#666';
    }
}

// Загрузка списка миссий
async function loadMissions() {
    missionsList.innerHTML = '<div class="loading">Загрузка списка...</div>';
    
    try {
        const response = await fetch(`${API_BASE}/missions`);
        
        if (!response.ok) {
            const errorText = await response.text();
            missionsList.innerHTML = `<div class="error">Ошибка загрузки списка: ${errorText}</div>`;
            return;
        }
        
        const text = await response.text();
        let missions = [];
        
        try {
            missions = JSON.parse(text);
        } catch (e) {
            console.error("Не удалось распарсить JSON миссий:", e);
            missionsList.innerHTML = `<div class="error">Ошибка формата данных от сервера</div>`;
            return;
        }
        
        if (!Array.isArray(missions) || missions.length === 0) {
            missionsList.innerHTML = '<div class="empty-message">Архив пуст. Загрузите первую миссию.</div>';
            return;
        }
        
        let html = '<table class="missions-table">';
        html += '<thead><tr>';
        html += '<th>ID</th><th>Mission ID</th><th>Дата</th><th>Локация</th><th>Результат</th><th>Ущерб</th><th></th>';
        html += '</tr></thead><tbody>';
        
        for (const m of missions) {
            html += '<tr>';
            html += `<td>${m.id || '-'}</td>`;
            html += `<td>${m.missionId || '-'}</td>`;
            html += `<td>${m.date || '-'}</td>`;
            html += `<td>${m.location || '-'}</td>`;
            html += `<td>${m.outcome || '-'}</td>`;
            html += `<td>${m.damageCost ? m.damageCost + ' ¥' : '-'}</td>`;
            html += `<td><button class="delete-btn" data-id="${m.id}">Удалить</button></td>`;
            html += '</tr>';
        }
        html += '</tbody></table>';
        missionsList.innerHTML = html;
        
        // Добавляем обработчики на кнопки удаления
        document.querySelectorAll('.delete-btn').forEach(btn => {
            btn.addEventListener('click', () => deleteMission(parseInt(btn.dataset.id)));
        });
        
    } catch (error) {
        missionsList.innerHTML = `<div class="error">Ошибка загрузки: ${error.message}</div>`;
        console.error(error);
    }
}

// Удаление миссии
async function deleteMission(id) {
    if (!confirm(`Удалить миссию с ID ${id}?`)) return;
    
    try {
        const response = await fetch(`${API_BASE}/missions/${id}`, {
            method: 'DELETE'
        });
        
        if (response.ok) {
            loadMissions();
            if (reportId.value == id) {
                reportResult.innerHTML = '';
                reportId.value = '';
            }
        } else {
            const errorText = await response.text();
            alert('Ошибка при удалении: ' + errorText);
        }
    } catch (error) {
        alert('Ошибка сети: ' + error.message);
    }
}

async function generateReport() {
    const id = reportId.value.trim();
    
    if (!id) {
        reportResult.innerHTML = '<div class="error">Введите ID миссии</div>';
        return;
    }
    
    const format = reportFormat.value;
    reportResult.innerHTML = '<div class="loading">Формирование отчета...</div>';
    
    try {
        const response = await fetch(`${API_BASE}/missions/${id}/report?format=${format}`);
        
        if (response.ok) {
            const reportText = await response.text();
            reportResult.innerHTML = `<pre style="margin:0; white-space:pre-wrap; font-family:monospace; background:#f8f9fa; padding:12px; border-radius:8px;">${escapeHtml(reportText)}</pre>`;
        } else if (response.status === 404) {
            reportResult.innerHTML = `<div class="error">Миссия с ID ${id} не найдена</div>`;
        } else {
            const errorText = await response.text();
            reportResult.innerHTML = `<div class="error">Ошибка: ${errorText}</div>`;
        }
    } catch (error) {
        reportResult.innerHTML = `<div class="error">Ошибка сети: ${error.message}</div>`;
        console.error(error);
    }
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Загружаем миссии при старте
loadMissions();