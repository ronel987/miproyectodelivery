$(function () {
    // adicionando drag and drop a diálogos
    $('.modal-parainfo').on('shown.bs.modal', function () {
        $(this).find('.modal-header').drags();
    });
});

function mouseover(id) {
    $("#" + id).attr("type", "text");
}

function mouseout(id) {
    $("#" + id).attr("type", "password");
}

function passwordChange() {
    var password = $("#password").val();
    var password1 = $("#password1").val();
    var password2 = $("#password2").val();

    if ($.trim(password) === "" || $.trim(password1) === "" || $.trim(password2) === "") {

        $("#password_error").html("Debe ingresar los <span class=\"fw-bold\">3 passwords</span>.");
        $("#password_error").show();
        return;
    }

    if (password1 !== password2) {

        $("#password_error").html("<span class=\"fw-bold\">Password nuevo</span> y <span class=\"fw-bold\">Password nuevo (confirmar)</span> deben ser iguales.");
        $("#password_error").show();
        return;
    }


    let idusuario = sessionStorage.getItem('idusuario');
    let passwords = btoa(idusuario + " - " + password + " - " + password1 + " - " + password2); // base64

    $("#password_ajax_wait").show();
    $.ajax({
        url: "http://173.249.20.156:8080/minimarket/rest/usuarios/password",
        headers: {
            passwords: passwords
        },
        dataType: "json",
        type: "PUT",
        success: function (result) { //alert(JSON.stringify(result));
            $("#password_ajax_wait").hide();

            $("#dlg_message_msg").text(result.message);
            $("#dlg_message").modal("show");
        }
    });
}