$ ->
  $('#contract_story_write').click () ->
    location.href = '/admin/contract_story_write'

  $('#filter_category').change () ->
    getContractList()

  getContractList()

@getContractList = () ->
  category = $('#filter_category').val()
  page = getCurrentPage()
  url = $('#list_container').data("url")
  $.get "#{url}/#{category}/#{page}",
    (data, status) ->
      afterAjaxLoad(data)

@pageMove = () ->
  getContractList()

