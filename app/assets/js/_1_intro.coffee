$ ->
  $('.people .photo').each( () ->
#    $(this).css 'background-color', 'yellow'
    $(this).css 'background-image', "url('/assets//images/contents/people/" + this.id + ".jpg')"
  )

