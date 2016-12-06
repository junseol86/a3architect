$ ->
  $('#contract_story_write').click () ->
    location.href = '/admin/contract_story_write'

  getAdminContractList(1, 1)

@getAdminContractList = () ->
  category = 1
  page = if $('#pages_container #pages #numbers .number.-on').length then $('#pages_container #pages #numbers .number.-on').text() else 1
  url = $('#list_container').data("url")
  console.log(url)
  $.get "#{url}/#{category}/#{page}",
    (data, status) ->
      $('#list_container').html(data)
      setPagesInterface()


@pageMove = () ->
  getAdminContractList()

