function validatePasswords(form) {
    const pass = document.getElementById('pass').value;
    const confirmPass = document.getElementById('confirm-pass').value;
    const passwordsAlert = document.getElementById('passwords-alert');
    if (pass === confirmPass) {
        form.submit();
    } else {
        if (passwordsAlert != null) {
            passwordsAlert.style.display = "block";
        }
    }
}