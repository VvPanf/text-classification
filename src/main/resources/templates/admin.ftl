<#import "parts/common.ftl" as c>

<@c.page>
<div class="container">
    <table class="table table-hover">
        <tr>
            <th scope="col">ID</th>
            <th scope="col">Дата запроса</th>
            <th scope="col">Пользователь</th>
            <th scope="col">Заголовок запроса</th>
            <th scope="col">Содержание запроса</th>
        </tr>
        <#if incidents?has_content>
            <#list incidents as incident>
                <tr>
                    <th scope="row">${incident.id}</th>
                    <td>${incident.timestamp}</td>
                    <td>${incident.user.username}</td>
                    <td>${incident.title}</td>
                    <td>${incident.text}</td>
                </tr>
            </#list>
        <#else>
            <tr>
                <td scope="row" colspan="5">Список запросов пуст</td>
            </tr>
        </#if>
    </table>
</div>
</@c.page>