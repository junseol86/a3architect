$ ->
  #슬라이드 하단 세개 점 중에 현 위치를 표시하기 위해 슬라이드 아이템마다 번호를 지정해놓음
  $('#slide_item_container .item:eq(0)').attr 'id', '0'
  $('#slide_item_container .item:eq(1)').attr 'id', '1'
  $('#slide_item_container .item:eq(2)').attr 'id', '2'

  #첫번째 슬라이드가 가운데 와야 하므로, 마지막 것을 떼어 맨 앞에 가져다놓음
  slide = $('#slide_item_container .item:eq(2)').detach()
  slide.insertBefore($('#slide_item_container .item:eq(0)'))

  $('#slide span').css 'color', 'white'
  $('#slide span:eq(0)').css 'color', 'black'

  $('#slide #left_btn').click ->
    slideLeft()
  $('#slide #right_btn').click ->
    slideRight()

  @timeout = setTimeout(slideLeft, 5000)

  $('#portfolio_tabs > div').click ->
    $('#portfolio_tabs > div').removeClass 'on'
    $(this).addClass 'on'
    getPortfolioList()
  getPortfolioList()

  $('#sites_tabs > div').click ->
    $('#sites_tabs > div').removeClass 'on'
    $(this).addClass 'on'
    getSitesList()
  getSitesList()

@timeout

@slideRight = () ->
  clearTimeout(@timeout)
  $('#slide_item_container').animate {
    left: "-=700px"
  }, 1000, () ->
    $('#slide_item_container').css 'left', '-700px'
    slide = $('#slide_item_container .item:eq(0)').detach()
    slide.insertAfter($('#slide_item_container .item:eq(1)'))
    slideOn = $('#slide_item_container .item:eq(1)').attr 'id'
    $('#slide span').css 'color', 'white'
    $('#slide span:eq(' + slideOn + ')').css 'color', 'black'

    clearTimeout(@timeout)
    @timeout = setTimeout(slideLeft, 5000)

@slideLeft = () ->
  clearTimeout(@timeout)
  $('#slide_item_container').animate {
    left: "+=700px"
  }, 1000, () ->
    $('#slide_item_container').css 'left', '-700px'
    slide = $('#slide_item_container .item:eq(2)').detach()
    slide.insertBefore($('#slide_item_container .item:eq(0)'))
    slideOn = $('#slide_item_container .item:eq(1)').attr 'id'
    $('#slide span').css 'color', 'white'
    $('#slide span:eq(' + slideOn + ')').css 'color', 'black'
    clearTimeout(@timeout)
    @timeout = setTimeout(slideLeft, 5000)


@getPortfolioList = () ->
  category = $('#portfolio_tabs > div.on').attr 'id'
  page = boardModule().getCurrentPage()
  url = $('#portfolio_list_container').data("url")
  $.post url,
    {
      category: category
    }
    (data, status) ->
      boardModule().afterAjaxLoadSpecific(data, '#portfolio_list_container')

@getSitesList = () ->
  category = $('#sites_tabs > div.on').attr 'id'
  page = boardModule().getCurrentPage()
  url = $('#sites_list_container').data("url")
  $.post url,
    {
      category: category
    }
    (data, status) ->
      boardModule().afterAjaxLoadSpecific(data, '#sites_list_container')
