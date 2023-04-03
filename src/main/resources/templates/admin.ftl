<#import "parts/common.ftl" as c>

<@c.page>

   <div class = "sidebar">
      <ul class="list">
          <li class="list-item">
              <a href="/admin?type=new">
                  <ion-icon name="newspaper-outline"></ion-icon>
                  <span class="links-name">Новые заявки</span>
              </a>
          </li>
          <li class="list-item">
              <a href="/admin?type=rejected">
                  <ion-icon name="alert-circle-outline"></ion-icon>
                  <span class="links-name">Отклоненные заявки</span>
              </a>
          </li>
          <li class="list-item">
              <a href="/admin">
                  <ion-icon name="file-tray-full-outline"></ion-icon>
                  <span class="links-name">Полный список</span>
              </a>
          </li>
      </ul>
   </div>

    <div class="wrapper_admin_full">
    <span class ="icon-close"> <ion-icon name="close"></ion-icon> </span>
    <div class="admin_text">
        <h2> ${title} </h2>

        <div class="admin_table">
            <table class="table">
                <tr>
                    <th>ID</th>
                    <th>Дата запроса</th>
                    <th>Пользователь</th>
                    <th>Заголовок запроса</th>
                    <th>Содержание запроса</th>
                    <th>Рабочая группа</th>
                    <th>Статус</th>
                    <th>-</th>
                </tr>
                <#if incidents?has_content>
                    <#list incidents as incident>
                        <tr>
                            <td width="5%">${incident?counter}</td>
                            <td width="10%">${incident.timestamp?string.short!""}</td>
                            <td width="7%">${incident.user.username!""}</td>
                            <td width="12.5%">${incident.title!""}</td>
                            <td width="20%">${incident.text!""}</td>
                            <td width="10%">${incident.workGroup!""}</td>
                            <td width="10%">${incident.status!""}</td>
                            <td width="5%">
                                <a class="edit-btn" href="/incident?id=${incident.id}">
                                    <ion-icon name="pencil-outline"></ion-icon>
                                </a>
                                <a class="delete-btn" href="/admin?delete=${incident.id}">
                                    <ion-icon name="close"></ion-icon>
                                </a>
                            </td>
                        </tr>
                    </#list>
                <#else>
                    <tr>
                        <td scope="row" colspan="8">Список запросов пуст</td>
                    </tr>
                </#if>
            </table>

        </div>
    </div>
</@c.page>