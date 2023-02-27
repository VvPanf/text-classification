<#import "parts/common.ftl" as c>

<@c.page>
<div class="container">
    <form class="form" method="POST">
        <h3>Форма для отправки сообщения</h3>
        <#if errors??>
            <#list errors as error>
                <div class="alert alert-danger">
                    ${'Поле ' + error.field + ' ' + error.defaultMessage}
                </div>
            </#list>
        </#if>
        <input class="form-control" type="text" placeholder="Введите заголовок обращения..." name="title" value="${(inputIncident.title)!''}">
        <textarea class="form-control" rows="10" placeholder="Введите текст обращения..." name="text">${(inputIncident.text)!''}</textarea>
<!--        <input type="file" name="file">-->
        <input class="btn btn-primary" type="submit" value="Отправить">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
    </form>
    <#if incidents??>
        <h3>Ваши заявки</h3>
        <#list incidents as incident>
            <textarea class="form-control" disabled>
                ${incident.title}
                ${incident.text}
                ${incident.timestamp}
            </textarea>
        </#list>
    <#else>
        <h3>У вас нет заявок</h3>
    </#if>
</div>
</@c.page>