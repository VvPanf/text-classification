<#import "parts/common.ftl" as c>

<@c.page>
    <div class="wrapper">
        <div class="form-box login">
            <h2>Авторизация</h2>
            <#if SPRING_SECURITY_LAST_EXCEPTION??>
                <div class="alert alert-danger">
                    ${SPRING_SECURITY_LAST_EXCEPTION.message}
                </div>
            </#if>
            <form method="POST">
                <div class="input-box">
                 <span class="icon">
                    <ion-icon name="mail-outline"></ion-icon>
                 </span>
                    <input type="text" name="username" required>
                    <label>Логин</label>
                </div>
                <div class="input-box">
               <span class="icon">
                    <ion-icon name="lock-closed-outline"></ion-icon>
               </span>
                    <input type="password" name="password" required>
                    <label>Пароль</label>
                </div>
                <div class="remember-forgot">
                    <label> <input type="checkbox">Запомнить пароль</label>
                </div>
                <div class="buttonLog">
                    <button type="submit" class="btn">Войти</button>
                </div>
                <input type="hidden" name="_csrf" value="${_csrf.token}" />
            </form>
        </div>
    </div>
</@c.page>