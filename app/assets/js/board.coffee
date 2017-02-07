#게시판 형식으로 된 페이지들의 공통적 부분들을 처리하는 스크립트
$ ->
  #이미지를 이미지 서버로 올리고 그로부터 회신 메시지를 받았을 때, 임시제거한 이미지 input을 다시 넣어준다
  window.addEventListener('message', boardModule().asyncReceiveMessage, false)

  #ajax로 이미지를 올리는 작업이 있는 페이지에서, 해당 작업을 실행하기 위한 input 요소들을 넣어준다.
  boardModule().putPickPhotoForm()
  boardModule().putPickThumbnailForm()
  boardModule().putPickInChargePhotoForm()
  boardModule().putPickPicsForm()
  boardModule().putPickSceneForm()

@boardModule = () -> {
  setPagesInterface: () ->
    pageNumbers = $('#pages_container #pages #numbers .number')
    pagesCount = pageNumbers.length
    pageNoWidth = pageNumbers.width()
    pageNoScroll = $('#pages_container #pages #numbers')

    #게시판의 페이지 이동
    pageNumbers.click () ->
      location.hash = ''
      $(this).siblings().removeClass '-on'
      $(this).addClass '-on'
      pageMove()
    if (pagesCount <= 5)
      $('#pages_container img').css 'display', 'none'
      $('#pages_container #pages').css 'width', pageNoWidth * pagesCount + 'px'
      $('#pages_container #numbers').css 'width', pageNoWidth * pagesCount + 'px'
    else
      $('#pages_container #pages #numbers #numbers_lined').css 'width', pageNoWidth * pagesCount + 'px'
      pageNoScroll.scrollLeft(($('#pages_container #pages #numbers .number.-on').text() - 3) * pageNoWidth)

    $('#pages_container #pages #move_right').click () ->
      pageNoScroll.animate({scrollLeft: '+=' + pageNoScroll.width()}, 300);
    $('#pages_container #pages #move_left').click () ->
      pageNoScroll.animate({scrollLeft: '-=' + pageNoScroll.width()}, 300);

  #게시판 리스트들은 ajax를 통해 페이지 내용을 받은 뒤 #list_container란 아이디의 div 내에 삽입된다.
  afterAjaxLoad: (data) ->
    this.afterAjaxLoadSpecific(data, '#list_container')

  afterAjaxLoadSpecific: (data, id) ->
    $(id).html(data)
    this.setPagesInterface()
    $('.url_link').click () ->
      location.hash = boardModule().getCurrentPage() + getExtraHash()
      location.href = $(this).data('url')

  getCurrentPage: () ->
  #  해시를 없앴을 때 타 브라우저는 '', 익스플로러는 '#'을 남기므로 제거한다.
    if (location.hash.replace('#', '') != '')
      hashData = location.hash
      hashData.replace('#', '').split('^')[0]
    else
      pageOn = $('#pages_container #pages #numbers .number.-on')
      if pageOn.length then pageOn.text() else 1

  #익스플로러 10 이하에서는 ajax로 파일을 업로드할 수 없다.  따라서, 새로고침 없이 비동기로 파일 업로드를 진행하기 위해서,
  #보이지 않는 iframe을 만들고 여기서 파일을 업로드한 뒤, 그 결과를 postMessage() 콜백으로 받아 호출한다.
  asyncFileUpload: (file, action) ->
    if (file.val() == '')
      alert '사진 파일을 선택하세요.'
      return
    targetIframe = 'upload_iframe'
    uploadForm = $("<form id=\"uploadForm\" action=\"#{action}\" method=\"post\" enctype=\"multipart/form-data\" style=\"display:none;\" target=\"#{targetIframe}\"></form>")
    $('body').append(uploadForm)
    file.appendTo(uploadForm)

    uploadIframe = $("<iframe id=\"uploadIframe\" src=\"javascript:false;\" name=\"#{targetIframe}\" style=\"display:none;\"><iframe>")
    $('body').append(uploadIframe)

    uploadForm.submit()

  #이미지를 업로드하고 이미지 서버에서 응답을 받으면, 이미지를 올릴 때 사용된 임시 input과 iframe 삭제한다.
  asyncReceiveMessage: (event) ->
    boardModule().asyncFileUploadCallback event.data
    $('#uploadForm').remove()
    $('#uploadIframe').remove()

  asyncFileUploadCallback: (result) ->
    if (result == 'NOT IMAGE')
      alert '이미지 파일(jpg, png, 또는 gif)을 첨부하세요.'
      boardModule().putPickPhotoForm()
    else if (result.indexOf('@') < 0 && result.indexOf('$') < 0 && result.indexOf('#') < 0)
      image = "<img src=\"http://#{result}\" />"
      oEditors.getById["ir1"].exec("PASTE_HTML", [image])
      boardModule().putPickPhotoForm()
#    게시판 내용으로 들어가는 사진이 아닐 경우
    else
      uploadedImageProcess(result)

  #페이지마다 업로드하는 이미지들이 다르므로, 요소 존재 여부에 따라 중에서 실행된다.

  putPickPhotoForm: () ->
    uploadPhotoBtn = $('#photoUploadBtn')
    if (uploadPhotoBtn.length > 0)
      pickPhotoForm = $("<input id=\"photoInput\" type=\"file\" name=\"picture\"/>")
      pickPhotoForm.insertBefore(uploadPhotoBtn)

  putPickThumbnailForm: () ->
    uploadThumbnailBtn = $('#thumbnailUploadBtn')
    if (uploadThumbnailBtn.length > 0)
      pickThumbnailForm = $("<input id=\"thumbnailInput\" type=\"file\" name=\"picture\"/>")
      pickThumbnailForm.insertBefore(uploadThumbnailBtn)

  putPickInChargePhotoForm: () ->
    uploadInChargePhotoBtn = $('#inChargePhotoUploadBtn')
    if (uploadInChargePhotoBtn.length > 0)
      pickInChargePhotoForm = $("<input id=\"inChargePhotoInput\" type=\"file\" name=\"picture\"/>")
      pickInChargePhotoForm.insertBefore(uploadInChargePhotoBtn)

  putPickPicsForm: () ->
    uploadPicsBtn = $('#picsUploadBtn')
    if (uploadPicsBtn.length > 0)
      pickPicsForm = $("<input id=\"picsInput\" type=\"file\" name=\"picture\" multiple/>")
      pickPicsForm.insertBefore(uploadPicsBtn)

  putPickSceneForm: () ->
    uploadPicsBtn = $('#sceneUploadBtn')
    if (uploadPicsBtn.length > 0)
      pickPicsForm = $("<input id=\"sceneInput\" type=\"file\" name=\"picture\" multiple/>")
      pickPicsForm.insertBefore(uploadPicsBtn)
}

