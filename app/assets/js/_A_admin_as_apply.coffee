$ ->
  $('#as_apply_write').click () ->
    location.href = '/admin/as_apply_write'
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
