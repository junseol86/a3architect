$ ->
  getAsApplyList()

#페이징 이외의 해시가 필요할 때.  없으면 '' 반환
@getExtraHash = () ->
  ''

@getAsApplyList = () ->
  page = boardModule().getCurrentPage()
  url = $('#list_container').data("url")
  $.post url,
    {
      page: page
    }
    (data, status) ->
      boardModule().afterAjaxLoad(data)

@pageMove = () ->
  getAsApplyList()

@uploadedImageProcess = (result) ->
  url = result.replace('@', '')
  $('#aa_thumbnail').css 'background-image', "url('http://#{url}')"
  imageProcessor().image_process('#aa_thumbnail')
  $('#thumbnail_to_submit').val(url)
  boardModule().putPickThumbnailForm()

@write_as_apply_toggle = () ->
  $('#as_apply_form').css 'display', if $('#as_apply_form').css('display') == 'none' then 'block' else 'none'
  $('#write_btn_area').css 'display', if $('#write_btn_area').css('display') == 'none' then 'block' else 'none'

@submit_as_apply = () ->
  $('.project #modified_input').val(new Date().getTimeString())
  document.as_apply_form.submit()
