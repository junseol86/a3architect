$ ->
  $('.circles > div').click () ->
    $(this).siblings().removeClass('-on')
    $(this).addClass('-on')
    getAdminContractList()

  getAdminContractList()

@getAdminContractList = () ->
  category = $('.circles > div.-on').data('category')
  page = if $('#pages_container #pages #numbers .number.-on').length then $('#pages_container #pages #numbers .number.-on').text() else 1
  url = $('#list_container').data("url")
  console.log("#{url}/#{category}/#{page}")
  $.get "#{url}/#{category}/#{page}",
    (data, status) ->
      $('#list_container').html(data)
      setPagesInterface()


@pageMove = () ->
  getAdminContractList()
