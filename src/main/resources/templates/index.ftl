<#import "parts/common.ftl" as c>

<@c.page>
<div class="container">
    <form class="form" method="POST">
        <h3>Форма для отправки сообщения</h3>
        <textarea placeholder="Напишите что-нибудь..." name="text"></textarea>
<!--        <input type="file" name="file">-->
        <input type="submit" value="Отправить">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
    </form>
    <textarea disabled>Ваш текст: ${output!"n/a"}</textarea>
</div>
</@c.page>