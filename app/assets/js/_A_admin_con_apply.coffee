$ ->
  $('#filter_category').change () ->
    location.hash = ''
    getConApplyList()

  $('#filter_category').val(getCategory())
  getConApplyList()

@getExtraHash = () ->
  '^' + getCategory()

@setCategoryInterface = (category) ->
  $('#filter_category').val(category)


@getCategory = () ->
  if (location.hash.replace('#', '') != '')
    hashData = location.hash
    hashData.replace('#', '').split('^')[1]
  else
    $('#filter_category').val() || 1


@getConApplyList = () ->
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
  getConApplyList()

