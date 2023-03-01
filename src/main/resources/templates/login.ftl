<#import "parts/common.ftl" as c>

<@c.page>
    <div class="container">
        <form class="form" method="POST">
            <h3>Вход в систему</h3>
            <#if SPRING_SECURITY_LAST_EXCEPTION??>
                <div class="alert alert-danger">
                    ${SPRING_SECURITY_LAST_EXCEPTION.message}
                </div>
            </#if>
            <input class="form-control" type="text" name="username" placeholder="Username">
            <input class="form-control" type="password" name="password" placeholder="Password">
            <input class="btn btn-primary" type="submit" value="Войти">
            <a class="btn" href="/registration">Регистрация</a>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
        </form>
    </div>
</@c.page>