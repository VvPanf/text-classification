<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Text Classification</title>
        <link rel="stylesheet" href="/static/style.css">
    </head>
    <body>
        <header>
            <div class = "logo_img">
                <ion-icon name="logo-gitlab"></ion-icon>
            </div>
            <h2 class="logo">Service desk</h2>
            <nav class="navigation">
                <a href="/">Главная</a>
                <a href="/create">Создать инцидент</a>
                <a href="/incidents">Мой список инцидентов</a>
                <#if user?? && user.isAdmin()>
                    <a href="/admin">Управление инцидентами</a>
                </#if>
                <#if user??>
                    <div>
                        <form action="/logout" method="POST">
                            <button class="btnLogin" type="submit">Выйти</button>
                            <input type="hidden" name="_csrf" value="${_csrf.token}" />
                        </form>
                    </div>
                <#else>
                    <div>
                        <form action="/login" method="GET">
                            <button class="btnLogin" type="submit">Войти</button>
                        </form>
                    </div>
                </#if>
            </nav>


    </header>
    <#nested>
    <script type="module" src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.js"></script>
    <script src="static/script.js"></script>
    </body>
    </html>
</#macro>