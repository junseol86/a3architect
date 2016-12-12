$ ->
  setCategoryInterface(getCategory())

  $('.left_menu > div').click () ->
    setCategoryInterface($(this).data('category'))
    location.hash = ''
    getPromotionList()

  getPromotionList()

@setCategoryInterface = (category) ->
  $.each $('.left_menu > div'), (idx, obj) ->
    $(obj).removeClass('-on')
    if ($(obj).data('category') && $(obj).data('category').toString() == category.toString())
      $(obj).addClass('-on')

#페이징 이외의 해시가 필요할 때.  없으면 '' 반환
@getExtraHash = () ->
  ''

@getCategory = () ->
  if (location.hash.replace('#', '') != '')
    hashData = location.hash
    hashData.replace('#', '').split('^')[1]
  else
    $('.left_menu > div.-on').data('category') || 1

@getPromotionList = () ->
  page = boardModule().getCurrentPage()
  url = $('#list_container').data("url")
  $.post url,
    {
      category: getCategory()
      page: page
    }
    (data, status) ->
      boardModule().afterAjaxLoad(data)

@pageMove = () ->
  getPromotionList()

