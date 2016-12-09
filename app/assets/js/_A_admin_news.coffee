$ ->
  $('#news_write').click () ->
    location.href = '/admin/news_write'
  getNewsList()

#페이징 이외의 해시가 필요할 때.  없으면 '' 반환
@getExtraHash = () ->
  ''

@getNewsList = () ->
  page = boardModule().getCurrentPage()
  url = $('#list_container').data("url")
  $.post url,
    {
      page: page
    }
    (data, status) ->
      boardModule().afterAjaxLoad(data)

@pageMove = () ->
  getNewsList()
