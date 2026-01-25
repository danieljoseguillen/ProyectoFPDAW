    document.addEventListener('DOMContentLoaded', function () {
        const toasts = document.querySelectorAll('.alert ');
        toasts.forEach(t => {
            const alert = new bootstrap.Alert(t);
            setTimeout(() => {
            alert.close();
        }, 5000);
        });
    });
