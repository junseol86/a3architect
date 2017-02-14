$ ->

#about

  $('#sites_tabs > div').click ->
    $(this).siblings().removeClass('on')
    $(this).addClass('on')
    selected = $(this).attr 'id'
    $('.section').css 'display', 'none'
    $('#' + selected + '_section').css 'display', 'block'



  $('.about_a').mouseenter ->
    $(this).children('div').children('img').each (index, element) ->
      $(element).attr 'src', $(element).attr('src').replace('_off', '_on')

  $('.about_a').mouseleave ->
    $(this).children('div').children('img').each (index, element) ->
      $(element).attr 'src', $(element).attr('src').replace('_on', '_off')

  $('#about_circles .circle').each (index, element) ->
    $(element).click ->
      showAboutPopup(index)
      $('#popup #department').data 'index', index
      showDepartment()

  $('#department_left').click (event) ->
    event.stopPropagation()
    switchDepartment (index) ->
      (--index+5)%5

  $('#department_right').click (event) ->
    event.stopPropagation()
    switchDepartment (index) ->
      ++index%5


#  A3 사람들
  $('.people_gallery .photo').each( () ->
    $(this).css 'background-image', "url('/assets/images/contents/people/" + this.id + ".jpg')"
    $(this).children('img').css 'background-image', "url('/assets/images/contents/people/" + this.id + ".png')"
  )
  $('.people_gallery > div').each( () ->
    idReplace = $(this).attr('id').replace(' ', '').replace('&', '')
    $(this).attr 'id', idReplace
  )
  $('.filters .items > div').click () ->
    idToShow = $(this).attr 'id'
    $('.people_gallery > div').removeClass '-on'
    $('.people_gallery > div#' + idToShow).addClass '-on'
    if (idToShow == 'A3BOX')
      $('.people_gallery > div').addClass '-on'
  $('.filters #all').click ->
    $('.people_gallery > div').addClass '-on'

#  Contact
  $('.contact #contact_info_btn').click ->
    $('.contact .contact_wrapper').removeClass('-company_view')
    $('.contact .contact_wrapper').addClass('-contact_info')

  $('.contact #company_view_btn').click ->
    $('.contact .contact_wrapper').removeClass('-contact_info')
    $('.contact .contact_wrapper').addClass('-company_view')
    imageProcessor().image_process '#view_large'
    for x in [1..6]
      imageProcessor().image_process '#thumb_' + x

  $('.contact #company_view li').mouseenter ->
    view_id = $(this).attr 'id'
    company_view_choice view_id.replace('li_', '')

  $('.contact .thumbnails div').mouseenter ->
    view_id = $(this).attr 'id'
    company_view_choice view_id.replace('thumb_', '')

  $('.contact #company_view .thumbnails > div').each () ->
    thumb_id = $(this).attr('id')
    img_id = thumb_id.replace('thumb_', '')
    $('#' + thumb_id).css "background-image", "url('/assets/images/contents/company_view/" + img_id + ".jpg')"


  # Contact 회사 전경
  company_view_choice = (id) ->
    $('.contact #view_large').css 'background-image', "url('/assets/images/contents/company_view/" + id + ".jpg')"
    $('#company_view ul li').removeClass('-on')
    $('#li_' + id).addClass('-on')
    $('.thumbnails div').removeClass('-on')
    $('#thumb_' + id).addClass('-on')

@departments = [
  {
    work: 'DEVELOPERS'
    name: '㈜A3D&D'
    alias: '부동산개발사업부'
    brief: '홍보, 임대, 분양업무, 시장조사, 사업성 분석'
    desc: '토지매입단계부터 사업성 분석과 함께 시공완료 후 임대까지 계획사고 실행합니다. A3를 믿고 설계, 시공을 맡겨주신 고객님을 대상으로 다수의 지역 공인중개사와 협업 시스템을 통하여 분양, 임대 의뢰 건축물에 대하여 빠르고 적절한 중개를 도와드립니다.'
  }
  {
    work: 'BUILDING CONSTRUCTION'
    name: '㈜A3건설'
    alias: '건설사업부'
    brief: '현장관리, 적산 및 견적, 하도급계약'
    desc: '성실하고 투명한 현장을 책임집니다. 다년간 건축경험을 가진 기술진들이 건축현장을 관리하며 일을 확실하고 신속히 진행합니다. 또한 합리적인 시공을 위해 가성비 좋은 자재와 제품을 사용하고 스펙북을 제공하여 더욱 투명한 견적과 시공을 실행합니다.'
  }
  {
    work: 'ARCHITECTURE DESIGN'
    name: '㈜건축사사무소A3'
    alias: '건축설계사업부'
    brief: '개발기획, 건축물경제성분석, 건축설계, 인허가 행정'
    desc: '꿈을 실현할 수 있는 가치 있는 공간을 설계합니다. 그 공간만의 아이덴티티로 창의적인 공간을 기획합니다. 건설사업부 및 디자인사업부와의 협력을 통해 합리적인 디자인을 연구하고 시도합니다. '
  }
  {
    work: 'INTERIOR DESIGN'
    name: '㈜A3디자인'
    alias: '인테리어사업부'
    brief: '실내공간디자인, 현장관리, 적산 및 견적, 하도급계약'
    desc: '스토리가 있는 스페이스 브랜딩을 통해 공간의 가치를 높이며 기능과 효율을 극대화하여 컨셉에 맞는 디자인가구, 조명, 소품 등을 활용하여 차별화된 공간을 창조합니다. '
  }
  {
    work: 'CONTAIN MODULAR'
    name: 'SPACE+SHIP'
    alias: '컨테이너사업부'
    brief: '개발기획, 모듈러건축 연구 및 설계, 공간디자인'
    desc: 'WE SHIP SPACE. IT CONTAINS PEOPLE AND STORES STORIES. 중고 컨테이너를 재활용함으로써 친환경건축을 시도하고 컨테이너의 장점 (저렴한 공사비, 짧은 시공기간, 이동의 편리성, 안정적 구조 등)을 활용한 모듈러 건축을 연구하여 다양한 공간을 디자인합니다.'
  }
]

@showDepartment = () ->
  index = $('#popup #department').data 'index'
  $('#department #work').text departments[index].work
  $('#department #name').text departments[index].name
  $('#department #alias').text departments[index].alias
  $('#department #brief').text departments[index].brief
  $('#department #desc').text departments[index].desc

@switchDepartment = (switchFunction) ->
  departmentIdx = $('#department').data 'index'
  $('#department').data 'index', switchFunction(departmentIdx)
  @showDepartment()
