    document.addEventListener('DOMContentLoaded', function () {
        const toasts = document.querySelectorAll('.toast');
        toasts.forEach(t => {
            const toast = new bootstrap.Toast(t);
            toast.show();
        });
    });
