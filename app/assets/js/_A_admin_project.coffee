$ ->
  $('#project_write').click () ->
    location.href = '/admin/project_write'
  getProjectList()

#페이징 이외의 해시가 필요할 때.  없으면 '' 반환
@getExtraHash = () ->
  ''

@getProjectList = () ->
  page = boardModule().getCurrentPage()
  url = $('#list_container').data("url")
  $.post url,
    {
      page: page
    }
    (data, status) ->
      boardModule().afterAjaxLoad(data)

@pageMove = () ->
  getProjectList()
