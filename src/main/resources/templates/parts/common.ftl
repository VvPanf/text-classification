<#macro page>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Text Classification</title>
        <link rel="stylesheet" href="/static/style.css">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-GLhlTQ8iRABdZLl6O3oVMWSktQOp6b7In1Zl3/Jr59b6EGGoI1aFkw7cmDA6j6gD" crossorigin="anonymous">
    </head>
    <body>
        <header>
            <h2 class="logo">Service desk</h2>
            <nav class="navigation">
                <a href="/">Главная</a>
                <a href="/create">Создать инцидент</a>
                <a href="/incidents">Мой список инцидентов</a>
                <#if user?? && user.isAdmin()>
                    <a href="/admin">Для разработчика</a>
                </#if>
                    <a href="/about">О нас</a>
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
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js" integrity="sha384-w76AqPfDkMBDXo30jS1Sgez6pr3x5MlQ1ZAGC+nuZB+EYdgRZgiwxhTBTkF7CXvN" crossorigin="anonymous"></script>
    <script type="module" src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule src="https://unpkg.com/ionicons@5.5.2/dist/ionicons/ionicons.js"></script>
    </body>
    </html>
</#macro>