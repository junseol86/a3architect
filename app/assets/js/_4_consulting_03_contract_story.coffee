$ ->
  $('.circles > div').click () ->
    setCategoryInterface($(this).data('category'))
    location.hash = ''
    getContractList()

  setCategoryInterface(getCategory())
  getContractList()

@setCategoryInterface = (category) ->
  $.each $('.circles > div'), (idx, obj) ->
    $(obj).removeClass('-on')
    if ($(obj).data('category') && $(obj).data('category').toString() == category.toString())
      $(obj).addClass('-on')

#해시도 없고 선택된 바도 없으면 1로 초기화
@getCategory = () ->
  if (location.hash.replace('#', '') != '')
    hashData = location.hash
    hashData.replace('#', '').split('^')[1]
  else
    $('.circles > div.-on').data('category') || 1

#페이징 이외의 해시가 필요할 때.  없으면 '' 반환
@getExtraHash = () ->
  '^' + getCategory()

@getContractList = () ->
  category = getCategory()
  page = boardModule().getCurrentPage()
  url = $('#list_container').data("url")
  $.post url,
    {
      category: category
      page: page
    }
    (data, status) ->
      boardModule().afterAjaxLoad(data)

@pageMove = () ->
  getContractList()

