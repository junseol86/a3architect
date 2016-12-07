$ ->
  $('#contract_story_write').click () ->
    location.href = '/admin/contract_story_write'

  $('#filter_category').change () ->
    getAdminContractList()

  getAdminContractList()

@getAdminContractList = () ->
  category = $('#filter_category').val()
  page = if $('#pages_container #pages #numbers .number.-on').length then $('#pages_container #pages #numbers .number.-on').text() else 1
  url = $('#list_container').data("url")
  console.log("#{url}/#{category}/#{page}")
  $.get "#{url}/#{category}/#{page}",
    (data, status) ->
      afterAjaxLoad(data)


@pageMove = () ->
  getAdminContractList()

