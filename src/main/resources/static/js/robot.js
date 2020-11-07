function googleCaptcha(){
    let response = grecaptcha.getResponse();
    let password = document.getElementById("password").value;
    let username = document.getElementById("username").value;

    if(response.length == 0 && (password.trim() != '' || username.trim() != '')){
        document.getElementById("recaptcha-error").innerHTML = `
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <span th:remove="tag">Please confirm you are not a robot.</span>
            <span type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </span>
        </div>`;
        return false;
    }
}

function verifyCaptcha(){
    document.getElementById("recaptcha-error").innerHTML = "";
}