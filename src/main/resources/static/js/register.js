$('document').ready(function (){
    let password = document.getElementById("password");
    let confirmPassword = document.getElementById("confirmPassword");

    function validatePassword(){
        if(password.value != confirmPassword.value){
            confirmPassword.append("<span>Passwords don't match</span>");;
        }else{
            confirmPassword.setCustomValidity("");
        }
    }

    password.onchange = validatePassword;
    confirmPassword.onkeyup = validatePassword;
});