$ ->
  window.addEventListener('message', asyncReceiveMessage, false)
  putPickPhotoForm()

@setPagesInterface = () ->
  pageNumbers = $('#pages_container #pages #numbers .number')
  pagesCount = pageNumbers.length
  pageNoWidth = pageNumbers.width()
  pageNoScroll = $('#pages_container #pages #numbers')

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

@afterAjaxLoad = (data) ->
  $('#list_container').html(data)
  setPagesInterface()
  $('.board_list_item').click () ->
    location.hash = getCurrentPage() + getExtraHash()
    location.href = $(this).data('url')

@getCurrentPage = () ->
#  해시를 없앴을 때 타 브라우저는 '', 익스플로러는 '#'을 남기므로 제거한다.
  if (location.hash.replace('#', '') != '')
    hashData = location.hash
    hashData.replace('#', '').split('^')[0]
  else
    pageOn = $('#pages_container #pages #numbers .number.-on')
    if pageOn.length then pageOn.text() else 1

#익스플로러 10 이하에서는 ajax로 파일을 업로드할 수 없다.  따라서, 새로고침 없이 비동기로 파일 업로드를 진행하기 위해서,
#보이지 않는 iframe을 만들고 여기서 파일을 업로드한 뒤, 그 결과를 postMessage() 콜백으로 받아 호출한다.
@asyncFileUpload = (file, action) ->
  targetIframe = 'upload_iframe'
  uploadForm = $("<form id=\"uploadForm\" action=\"#{action}\" method=\"post\" enctype=\"multipart/form-data\" style=\"display:none;\" target=\"#{targetIframe}\"></form>")
  $('body').append(uploadForm)
  file.appendTo(uploadForm)

  uploadIframe = $("<iframe id=\"uploadIframe\" src=\"javascript:false;\" name=\"#{targetIframe}\" style=\"display:none;\"><iframe>")
  $('body').append(uploadIframe)

  uploadIframe.on 'load', () ->
    ui = window.frames["upload_iframe"]
    console.log typeof ui
    console.log ui
    ui.contentWindow.postMessage("hello there!", "115.68.110.118/9000")

  uploadForm.submit()

@asyncReceiveMessage = (event) ->
  asyncFileUploadCallback event.data
  $('#uploadForm').remove()
  $('#uploadIframe').remove()


@asyncFileUploadCallback = (result) ->
  if (result == 'NOT IMAGE')
    alert '이미지 파일(jpg, png, 또는 gif)을 첨부하세요.'
  else
    image = "<img src=\"http://#{result}\" />"
    oEditors.getById["ir1"].exec("PASTE_HTML", [image])
  putPickPhotoForm()

@putPickPhotoForm = () ->
  uploadPhotoBtn = $('#photoUploadBtn')
  if (uploadPhotoBtn.length > 0)
    pickPhotoForm = $("<input type=\"file\" name=\"picture\"/>")
    pickPhotoForm.insertBefore(uploadPhotoBtn)

