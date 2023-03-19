<#import "parts/common.ftl" as c>

<@c.page>
    <div class="wrapper_admin">
    <span class ="icon-close"> <ion-icon name="close"></ion-icon> </span>
        <div class="admin_text">
            <h2> Мои инциденты </h2>

        <div class="admin_table">
             <table class="table table-hover">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Дата запроса</th>
                    <th scope="col">Заголовок запроса</th>
                    <th scope="col">Содержание запроса</th>
                    <th scope="col">Рабочая группа</th>
                    <th scope="col">Статус</th>
                </tr>
                <#if incidents?has_content>
                    <#list incidents as incident>
                        <tr>
                            <th scope="row">${incident.id}</th>
                            <td>${incident.timestamp}</td>
                            <td>${incident.title}</td>
                            <td>${incident.text}</td>
                            <td>${incident.workGroup}</td>
                            <td>${incident.status}</td>
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