$(function () {

    let token = sessionStorage.getItem('token');

    if (token === null || token === undefined || token === "") {
        window.location = "../";
    }

    $.ajax({
        url: "http://localhost:8080/restaurante/rest/autentica/valida",
        data: {
            token: token,
            autorizacion: "vende"
        },
        dataType: "json",
        success: function (result) { //alert(JSON.stringify(result));

            if (result.ok === false) {
                window.location = "../";
            }
        }
    });
});

function salir() {
    let idusuario = sessionStorage.getItem("idusuario");

    $.ajax({
        url: "http://localhost:8080/restaurante/rest/autentica/exit",
        data: {
            idusuario: idusuario
        },
        dataType: "json",
        success: function (result) { // alert(JSON.stringify(result));

            if (result.ok === true) {
                sessionStorage.clear();
                
                window.location = "../";
            }
        }
    });
}
