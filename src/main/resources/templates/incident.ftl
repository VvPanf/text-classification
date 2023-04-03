<#import "parts/common.ftl" as c>

<@c.page>
<div class="wrapper_index">
    <span class ="icon-close"> <ion-icon name="close"></ion-icon> </span>
    <form method="POST" enctype="multipart/form-data">
        <div class="form-box-in">
            <h2>Обращение</h2>
        </div>

        <div class="input-box-in">
            <h3>Заголовок: </h3>
            <div>${incident.title!""}</div>
        </div>
        <div class="text-box-in">
            <h3>Текст обращения: </h3>
            <textarea class="form-control" rows=8 name="text" disabled>${(incident.text)!""}</textarea>
        </div>
        <div class="info-box-in">
            <div class="text-box-in">
                <h3>Дата запроса: </h3>
                <div>${incident.timestamp?string.short!""}</div>
            </div>
            <div class="text-box-in">
                <h3>Пользователь: </h3>
                <div>${incident.user.username!""}</div>
            </div>
        </div>
        <div class="select-box-in">
            <h3>Рабочая группа: </h3>
            <select name="workGroup">
                <#list workGroupList as item>
                    <#if incident.workGroup == item>
                        <option value="${item}" selected>${item}</option>
                    <#else>
                        <option value="${item}">${item}</option>
                    </#if>
                </#list>
            </select>
        </div>
        <div class="button-box-in">
            <div class="buttonSend">
                <input class="btn" type="submit" value="Сохранить">
            </div>
        </div>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
    </form>
</div>
</@c.page>