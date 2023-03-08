<#import "parts/common.ftl" as c>

<@c.page>
  <div class="wrapper_index">
      <span class ="icon-close"> <ion-icon name="close"></ion-icon> </span>
    <form method="POST">
        <div class="form-box-in">
        <h2>Форма для создания обращения</h2>
        <#if errors??>
            <#list errors as error>
                <div class="alert alert-danger">
                    ${'Поле ' + error.field + ' ' + error.defaultMessage}
                </div>
            </#list>
        </#if>
        </div>
            <div class="input-box-in">
        <input class="form-control" type="text" placeholder="Введите заголовок обращения..." name="title" value="${(inputIncident.title)!''}">
            </div>
            <div class="text-box-in">
        <textarea class="form-control" rows=7 placeholder="Введите текст обращения..." name="text">${(inputIncident.text)!''}</textarea>
<!--        <input type="file" name="file">-->
            </div>
            <div class="buttonSend">
        <input class="btn" type="submit" value="Отправить">
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />

    <div class="bot-box">
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
        <h3>У вас пока нет отправленных заявок.</h3>
    </#if>
    </div>
    </form>
</div>
</@c.page>