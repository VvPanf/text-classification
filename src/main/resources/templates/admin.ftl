<#import "parts/common.ftl" as c>

<@c.page>

   <div class = "sidebar">
      <ul class="list">
          <li class="list-item">
              <a href="#">
                  <ion-icon name="newspaper-outline"></ion-icon>
                  <span class="links-name">Новые заявки</span>
              </a>
          </li>
          <li class="list-item">
              <a href="#">
                  <ion-icon name="alert-circle-outline"></ion-icon>
                  <span class="links-name">Отклоненные заявки</span>
              </a>
          </li>
          <li class="list-item">
              <a href="#">
                  <ion-icon name="file-tray-full-outline"></ion-icon>
                  <span class="links-name">Полный список</span>
              </a>
          </li>
      </ul>
   </div>

    <div class="wrapper_admin_full">
    <span class ="icon-close"> <ion-icon name="close"></ion-icon> </span>
    <div class="admin_text">
        <h2> Полный список </h2>

        <div class="admin_table">
            <table class="table table-hover">
                <tr>
                    <th scope="col">ID</th>
                    <th scope="col">Дата запроса</th>
                    <th scope="col">Получатель услуг</th>
                    <th scope="col">Срочность</th>
                    <th scope="col">Заголовок запроса</th>
                    <th scope="col">Содержание запроса</th>
                    <th scope="col">Рабочая группа</th>
                    <th scope="col">Статус</th>
                </tr>
                <#if incidents?has_content>
                    <#list incidents as incident>
                        <tr>
                            <th scope="row">${incident.id!""}</th>
                            <td>${incident.timestamp!""}</td>
                            <td>${incident.user.username!""}</td>
                            <td>Не срочно</td>
                            <td>${incident.title!""}</td>
                            <td>${incident.text!""}</td>
                            <td>${incident.workGroup!""}</td>
                            <td>${incident.status!""}</td>
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