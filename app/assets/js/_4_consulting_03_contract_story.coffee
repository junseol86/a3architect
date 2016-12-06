$ ->
  $('.circles > div').click () ->
    category = $(this).data('category')
    location.href = "/consulting/contract_story/#{category}"

  getAdminContractList()

@setCategory = (category) ->
  $.each $('.circles > div'), (idx, obj) ->
    $(obj).removeClass('-on')
    if ($(obj).data('category').toString() == category)
      $(obj).addClass('-on')

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

