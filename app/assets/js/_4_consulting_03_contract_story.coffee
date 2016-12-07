$ ->
  $('.circles > div').click () ->
    category = $(this).data('category')
    location.href = "/consulting/contract_story/#{category}"

  getContractList()

@setCategory = (category) ->
  $.each $('.circles > div'), (idx, obj) ->
    $(obj).removeClass('-on')
    if ($(obj).data('category').toString() == category)
      $(obj).addClass('-on')

@getContractList = () ->
  category = $('.circles > div.-on').data('category')
  page = getCurrentPage()
  url = $('#list_container').data("url")
  console.log("page:#{page} category:#{category}")
  $.post url,
    {
      category: category
      page: page
    }
    (data, status) ->
      afterAjaxLoad(data)

@pageMove = () ->
  getContractList()

