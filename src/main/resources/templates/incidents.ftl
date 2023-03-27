<#import "parts/common.ftl" as c>

<@c.page>
    <div class="wrapper_admin">
    <span class ="icon-close"> <ion-icon name="close"></ion-icon> </span>
        <div class="admin_text">
            <h2> Мои инциденты </h2>

        <div class="admin_table">
             <table class="table">
                <tr>
                    <th>ID</th>
                    <th>Дата запроса</th>
                    <th>Заголовок запроса</th>
                    <th>Содержание запроса</th>
                    <th>Рабочая группа</th>
                    <th>Статус</th>
                </tr>
                <#if incidents?has_content>
                    <#list incidents as incident>
                        <tr>
                            <td width="5%">${incident?counter}</td>
                            <td width="12.5%">${incident.timestamp?string.short!""}</td>
                            <td width="12.5%">${incident.title!""}</td>
                            <td width="30%">${incident.text!""}</td>
                            <td width="10%">${incident.workGroup!""}</td>
                            <td width="10%">${incident.status!""}</td>
                        </tr>
                    </#list>
                <#else>
                    <tr>
                        <td scope="row" colspan="6">Список запросов пуст</td>
                    </tr>
                </#if>
            </table>

        </div>
    </div>
</@c.page>