$ ->
  $('#contract_story_write').click () ->
    location.href = '/admin/contract_story_write'

  $('#filter_category').change () ->
    location.hash = ''
    getContractList()

  $('#filter_category').val(getCategory())
  getContractList()

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

@getContractList = () ->
  category = $('#filter_category').val()
  page = getCurrentPage()
  url = $('#list_container').data("url")
  $.get "#{url}/#{category}/#{page}",
    (data, status) ->
      afterAjaxLoad(data)

@pageMove = () ->
  getContractList()

