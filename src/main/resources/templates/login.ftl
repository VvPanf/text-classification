<#import "parts/common.ftl" as c>

<@c.page>
    <div class="container">
        <form class="form" method="POST">
            <input class="form-control" type="text" name="username" placeholder="Username">
            <input class="form-control" type="password" name="password" placeholder="Password">
            <input class="btn btn-primary" type="submit" value="Войти">
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
        </form>
    </div>
</@c.page>