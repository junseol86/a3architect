$ ->
  $('#space_story_write').click () ->
    location.href = '/admin/space_story_write'

  $('#filter_category').change () ->
    location.hash = ''
    getSpaceStoryList()

  $('#filter_category').val(getCategory())
  getSpaceStoryList()

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


@getSpaceStoryList = () ->
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
  getSpaceStoryList()

