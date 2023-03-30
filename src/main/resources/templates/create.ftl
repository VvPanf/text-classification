<#import "parts/common.ftl" as c>

<@c.page>
  <div class="wrapper_index">
      <span class ="icon-close"> <ion-icon name="close"></ion-icon> </span>
    <form method="POST" enctype="multipart/form-data">
        <div class="form-box-in">
        <h2>Форма для создания обращения</h2>
        <#if errors??>
            <#list errors as error>
                <div class="alert">
                    ${'Поле ' + error.field + ' ' + error.defaultMessage}
                </div>
            </#list>
        </#if>
        </div>

            <div class="input-box-in">
                <input class="form-control" type="text" placeholder="Введите заголовок обращения..." name="title" value="${(inputIncident.title)!''}">
            </div>
<!--        <div class="input-box-in">-->
<!--            <input class="form-control" type="text" placeholder="Введите контактное лицо..." name="title" value="${(inputIncident.title)!''}">-->
<!--        </div>-->
<!--        <div class="input-box-in">-->
<!--            <input class="form-control" type="text" placeholder="Укажите срочность" name="title" value="${(inputIncident.title)!''}">-->
<!--        </div>-->
<!--        <div class="input-box-in">-->
<!--            <input class="form-control" type="text" placeholder="Укажите подразделение исполнителя..." name="title" value="${(inputIncident.title)!''}">-->
<!--        </div>-->

            <div class="text-box-in">
                <textarea class="form-control" rows=8 placeholder="Введите текст обращения..." name="text">${(inputIncident.text)!''}</textarea>
            </div>
            <div class="button-box-in">
                <div class="upload-file__wrapper">
                    <input type="file" name="file" id="upload-file__input" class="upload-file__input">
                    <label class="upload-file__label" for="upload-file__input">
                        <svg class="upload-file__icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
                            <path d="M286 384h-80c-14.2 1-23-10.7-24-24V192h-87.7c-17.8 0-26.7-21.5-14.1-34.1L242.3 5.7c7.5-7.5 19.8-7.5 27.3 0l152.2 152.2c11.6 11.6 3.7 33.1-13.1 34.1H320v168c0 13.3-10.7 24-24 24zm216-8v112c0 13.3-10.7 24-24 24H24c-13.3 0-24-10.7-23-23V366c0-13.3 10.7-24 24-24h136v8c0 31 24.3 56 56 56h80c30.9 0 55-26.1 57-55v-8h135c13.3 0 24 10.6 24 24zm-124 88c0-11-9-20-19-20s-19 9-20 20 9 19 20 20 21-9 20-20zm64 0c0-12-9-20-20-20s-20 9-19 20 9 20 20 20 21-9 20-20z">
                            </path>
                        </svg>
                        <span class="upload-file__text">Прикрепить файл</span>
                    </label>
                </div>
                <div class="buttonSend">
                    <input class="btn" type="submit" value="Отправить">
                </div>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
    </form>
</div>
</@c.page>