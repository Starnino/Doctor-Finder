(function($) {
  "use strict";

/*******************************************************
 *
 * Custom Javascript for Horizon
 *
 *******************************************************/
var twitterUsername = 'envato',

	search_id = '',

	lat = 51.514617,
	lng = -0.190359;


/* ===================================================== */
/*	JS detection
/* ===================================================== */

var isMobile = {
	Android: function() {
		return navigator.userAgent.match(/Android/i);
	},
	BlackBerry: function() {
		return navigator.userAgent.match(/BlackBerry/i);
	},
	iOS: function() {
		return navigator.userAgent.match(/iPhone|iPad|iPod/i);
	},
	Opera: function() {
		return navigator.userAgent.match(/Opera Mini/i);
	},
	Windows: function() {
		return navigator.userAgent.match(/IEMobile/i);
	},
	any: function() {
		return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
	}
};


jQuery(document).ready(function($) {

	/* ===================================================== */
	/* Placeholders
	/* ===================================================== */
	$('input, textarea').placeholder();


	/* ===================================================== */
	/* Menu
	/* ===================================================== */

	// MENU FOR SMALL SCREENS
	$('.main_menu').attr('id', 'menu');
	selectnav('menu', {
		indent: '&nbsp;&nbsp;'
	});

	$('.main_menu li').hover(function() {
		$(this).find('ul').first().stop(true, true).fadeIn(500);
	}, function() {
		$(this).find('ul').first().stop(true, true).fadeOut(500);
	});

	// SMOOTH MENU JUMP
	$(".main_menu li, .down").click(function() {
		
		// Get the url from the clicked item or an anchor within
		var url = $(this).attr("href");
		 
		if ( url === undefined ) { url = $(this).find('a').attr("href");  }

		if (url.indexOf("#") >= 0 && $(url).length !== 0) {
			$(".main_menu a").removeClass('current');
			$(this).addClass('current');
			
			$.smooth(url, -60);
			return false;
		} else {
			window.location = url;
		}
	});

	// SOLID BACKGROUND SWITCH ON HOMEPAGE
	
	if ( $('.solid').length === 0) {
		var menuHeight = $('.main_menu').height();
		$(window).on('scroll', function() {
			var pos = jQuery(window).scrollTop();
			if (pos > (menuHeight + 100)) {
				$('.header').addClass('solid');
			} else {
				$('.header').removeClass('solid');
			}
		});
	}

	/* ===================================================== */
	/* Main Slider
	/* ===================================================== */

	// Animates the captions
	var animCaps = function(slide) {

		var delay;

		slide.each(function() {
			var elem = $(this),
				animation = elem.data('animation');

			elem.css({
				visibility: 'hidden'
			}).removeClass('animated ' + animation);

			delay = $(this).data('animate-delay');
			if (delay === undefined) {
				delay = '2000';
			}
			setTimeout(function() {
				elem.css({
					visibility: 'visible'
				}).addClass('animated ' + animation);
			}, delay);
		});
	};

	// Hides the animated captions
	$('.main-slider').find('.cap-anim').css({
		visibility: 'hidden'
	});

	//MAIN SLIDER OPTIONS
	$('.main-slider').bxSlider({
		auto: true,
		speed: 1000,
		pause: 8000,
		mode: 'fade',
		pager: false,
		controls: false,
		onSliderLoad: function(currentIndex) {
			animCaps($('.main-slider').find('li').eq(0).find('.cap-anim'));
		},
		onSlideBefore: function($slideElement, oldIndex, newIndex) {

			animCaps($slideElement.find('.cap-anim'));
		},
		onSlideAfter: function($slideElement) {

		}
	});


	/* ===================================================== */
	/* REVOLUTION SLIDER
	/* ===================================================== */

	if( $('.rev-slider').length ) {
		$('.rev-slider').revolution({
			hideTimerBar: 'on',
			fullWidth:"on",
			fullScreen:"on",
			navigationType: 'none',
			startwidth: 1200
		});
	}


	/* ===================================================== */
	/* VIDEO BACKGROUND										 */
	/* ===================================================== */

	if (isMobile.any() === null) {
		$(".player").mb_YTPlayer();
	} else {
		$(".slider-wrapper").addClass('video-placeholder');
	}


	/* ===================================================== */
	/*	Testimonials slider									 */
	/* ===================================================== */

	$('.testimonial').bxSlider({
		auto: true,
		speed: 1000,
		pause: 8000,
		mode: 'fade',
		pager: false,
		controls: false
	});


	/* ===================================================== */
	/*	Testimonials slider									 */
	/* ===================================================== */

	$('.post-slider').bxSlider({
		auto: true,
		speed: 1000,
		pause: 8000,
		mode: 'fade',
		pager: false,
		controls: true
	});


	/* ===================================================== */
	/*	Process slider
	/* ===================================================== */

	$('.process-slider').bxSlider({
		auto: true,
		speed: 1000,
		pause: 6000,
		mode: 'fade',
		pager: false,
		controls: false,
		onSlideBefore: function($slideElement, oldIndex, newIndex) {
			$('.process-list li').removeClass('current').eq(newIndex).addClass('current');

		}
	});


	/* ===================================================== */
	/*	Init parallax
	/* ===================================================== */

	if (isMobile.any() === null) {
		$('.parallax').css('backgroundAttachment', 'fixed').parallax('50%', 0.5, true);
	}


	/* ===================================================== */
	/*	Video Section
	/* ===================================================== */

	if (isMobile.any() === null) {
		$(".player").mb_YTPlayer();
	} else {
		$(".slider-wrapper").addClass('video-placeholder');
	}


	/* ===================================================== */
	/*	Lightbox
	/* ===================================================== */

	$("a[data-pp^='prettyPhoto']").prettyPhoto({
		hook: 'data-pp',
		theme: 'light_square',
		social_tools: ''
	});


	/* ===================================================== */
	/*	Contact Forms
	/* ===================================================== */

	$('.contact-form').contactValidation();


	/* ===================================================== */
	/*	Accordian, Tabs and toggles
	/* ===================================================== */

	$('.accordian').accordian();

	$('.tabs').tabs({
		type: 'top'
	});

	$('.sidetabs').tabs({
		type: 'side'
	});

	$('.toggle').toggle();

	 
	/* ===================================================== */
	/*	Alerts
	/* ===================================================== */

	$('.alert-button').alerts();

	/* ===================================================== */
	/*	Responsive videos
	/* ===================================================== */

	$('.video-wrapper').fitVids();

	//Fix z-index youtube video embedding
	$('.youtube iframe').each(function(){
		var url = $(this).attr("src");
		$(this).attr("src",url+"&wmode=transparent");
	});


	/* ===================================================== */
	/* Milestones
	/* ===================================================== */

	$('.milestones').appear(function() {
		$('.count').each(function() {
			var countTo = $(this).data('count');
			$(this).countTo({
				from: 0,
				to: countTo,
				speed: 2200,
				refreshInterval: 60
			});
		});

	});


	/*====================================================== */
	/*  Tooltips - Tipsy                                     */
	/* ===================================================== */

		$('[data-hook=tooltip]').tipsy({gravity: 's', fade: true});
		$('[data-hook=tooltip]').trigger('mouseOver');


	/* ===================================================== */
	/* Projects
	/* ===================================================== */

	var noItems = 4;

	$('.portfolio').waitForImages(function() {
		$('.portfolio').isotope({
			itemSelector: '.port-item',
			masonry: {
				columnWidth: $('.portfolio').width / 4
			},
			animationEnigin: 'best-availible',
			resizable: false,
			filter: '.showme'
		});
	});

	$(window).smartresize(function(){
		$('.portfolio').isotope({
			masonry: { columnWidth: $('.portfolio').width() / 4 }
		});
	});

	// LOAD MORE ITEMS
	$('#load-more').click( function(e) {
		e.preventDefault();
		
		if( noItems < $('.port-item').length ) {
			noItems += 4;
		}
		$('.port-item:lt(' + noItems + ')').addClass('showme');

		$('.portfolio').isotope({
			filter: '.showme'
		});

		
		
		if( noItems === $('.port-item').length ) {
			$(this).fadeOut();
		}

	});

	$('.filter-menu li').css({
		'cursor': 'pointer'
	});

$('.filter-menu li').trigger('click');
	$('.filter-menu li').click(function() {
		
		if (!$(this).hasClass('selected')) {
			var bgColor = $(this).css('background');
		}
		$(this).fadeTo('background', 'white');

		$(this).siblings().removeClass('selected');
		$(this).addClass('selected');
		var selector = $(this).attr('data-cat');
		var filter = '.isotope-item[data-cat=' + selector + ']';
		if (selector === '*') {
			filter = '.isotope-item';
		}

		$('.showme').removeClass('showme');
		
		$(filter + ':lt(' + noItems + ')').addClass('showme');

		$('.portfolio').isotope({
			filter: '.showme'
		});

	});

	// SHOW THE PROJECT
	var showProject = function(elem) {
		// Append project to wrapper
		var projectHTML = $('<div class="project">' + elem + '</div>').appendTo('.project-wrapper');

		closeButton();

		projectHTML.waitForImages(function() {
			setSlidler();
			var projectWrapper = $('.project-wrapper'),
				projectWrapperHeight = $('.project').outerHeight() + 110; // Get height of project
				$('.project').css('height', '100%'); // To fix ie visibility bug
				
			// Animate wrapper height, fade in project and remove loader
			projectWrapper.animate({
				height: projectWrapperHeight
			}, 600, function() {
				projectWrapper.css('height', 'auto'); // Remove fixed height
				$('.project').fadeIn('slow');
				projectWrapper.find('.loader i').fadeOut();
			});

			$('.project-wrapper').addClass('open');
			$('.port-overlay .project-btn').removeClass('disabled');
		});

	}; // End ShowProject

	// FETCH THE PROJECT
	var getProject = function(elem, scrollDfd, projectDfd) {
		
		$.get(elem.attr('href'), function(projectHTML) {
			
			projectDfd.resolve(projectHTML);
		}, 'html');
	}; // End getProject

	// ADD CLOSE BUTTON
	var closeButton = function() {
		$('.project-wrapper .container').prepend('<a href="#" class="close"><i class="fa fa-times-circle-o"></i></a>');
		$('.project-wrapper .close').click(function() {
			$('.project-wrapper').animate({
				height: 0
			}, function() {
				$('.loader').fadeOut('slow', function() {
					$(this).remove();
				});
			});
			$('.project-wrapper').removeClass('open');

			$('.project').fadeOut('slow', function() {
				$('.project').remove();
			});
			return false;
		});
	}; // End closeButton

	// PROJECT SLIDER INIT
	var setSlidler = function() {
		$('.portfolio-slider').bxSlider({
			auto: true,
			speed: 1000,
			pause: 8000,
			mode: 'fade',
		});
	}; // End setSlider

	// SWITCH TO ANOTHER PROJECT
	var changeProject = function(projectHTML, projectHeight) {
		$('.project-wrapper').css('height', $('.project-wrapper').outerHeight());
		$('.project').fadeOut('slow', function() {
			$('.project').remove();
			showProject(projectHTML);
		});
	}; // changeProject

	// SHOW A SPINNER
	var showLoader = function(projectWrapper) {
		// If loader is already present show spinner else add loader
		if (projectWrapper.find('.loader').length !== 0) {
			projectWrapper.find('.loader i').fadeIn();
		} else { // Else add preloader
			var preloadedHTML = '<div class="loader"><i class="fa fa-spinner spin"></i></div>';
			projectWrapper.animate({
				height: 50
			}).prepend(preloadedHTML);
		}
	}; // End showLoader

	// INIT PROJECT LOAD

	var projectInit = function(elem) {
		
		

		var projectWrapper = $('.project-wrapper'),
			scrollDfd = $.Deferred(),
			projectDfd = $.Deferred();
		showLoader(projectWrapper);
		getProject(elem, scrollDfd, projectDfd);
		$.smooth($('.loader'), -60, function() {
			scrollDfd.resolve();
		});

		$.when(projectDfd, scrollDfd).done(function(projectHTML) {
			if (projectWrapper.hasClass('open')) {
				changeProject(projectHTML);
			} else {
				showProject(projectHTML);
			}
		});

	}; // End projectInit

	$('.port-overlay .project-btn').on( "click", function( event ) {
		event.preventDefault();
		if( !$('.port-overlay .project-btn').hasClass('disabled') ) {
			$('.port-overlay .project-btn').addClass('disabled');
			projectInit( $(this) );
		}
	});


	/* ==================================================== */
	/*	Hexigons											*/
	/* ==================================================== */

	//FUNCTIONS

	var initHexs = function() {

		//TO ADD SVGS
		function SVG(tag) {
			return document.createElementNS('http://www.w3.org/2000/svg', tag);
		}

		var createHexPoints = function(numberOfSides, size, Xcenter, Ycenter) {
			var points;
			var point;

			for (var i = 1; i <= numberOfSides; i += 1) {
				point = (Xcenter + size * Math.cos(i * 2 * Math.PI / numberOfSides + 11));

				if (points === undefined) {
					points = Math.floor(point + 100);
				} else {
					points = points + ', ' + Math.floor(point + 100);
				}

				point = (Ycenter + size * Math.sin(i * 2 * Math.PI / numberOfSides + 11));
				points = points + ', ' + Math.floor(point + 100);
			}
			$('.notes').html(points);
			return points;
		};


		var i = 0,
		svgHTML;

		// CREATE HEXIGONS
		$('.feature-hex').each(function() {

			// Get img src and remove
			var hexImg = $(this).find('img'),
				imgSrc = hexImg.attr('src');
			hexImg.remove();

			// Add SVG HTML
			svgHTML = '<svg class="svg-graphic" width="200" height="200" viewBox="0 0 300 300" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" version="1.1">' +
				'<g><clipPath id="hex-mask' + i + '"></clipPath></g>' +
				'<g>' +
				'<image clip-path="url(#hex-mask' + i + ')" height="100%" width="100%" xlink:href="' + imgSrc + '" />' +
				'</g>' +
				'</svg>';
			$(this).append(svgHTML);

			i++;
		});


		var hexigonSvg = $('.svg-graphic');

		// GET VIEW BOX ATTRIBUTES
		var svg = document.getElementsByTagName('svg')[0];
		var box = svg.getAttribute('viewBox');
		box = box.split(' ');

		// GET POINTS FOR HEX MASK
		var numberOfSides = 6,
			size = box[3] / 2 - 20,
			Xcenter = box[3] / 2 - 100,
			Ycenter = box[3] / 2 - 100;
		var hexPoints1 = createHexPoints(numberOfSides, size, Xcenter, Ycenter);

		// ADD MASK HEX
		i = 0;
		$('.feature-hex').each(function() {
			var $svg = $('#hex-mask' + i);
			$(SVG('polygon'))
				.attr('points', hexPoints1)
				.appendTo($svg);

			i++;
		});

		// CREATE BORDER HEX
		numberOfSides = 6;
		size = box[3] / 2 - 2;
		Xcenter = box[3] / 2 - 100;
		Ycenter = box[3] / 2 - 100;
		var hexPoints2 = createHexPoints(numberOfSides, size, Xcenter, Ycenter);


		var $svg = $('.svg-graphic');
		$(SVG('polygon'))
			.attr('points', hexPoints2)
			.attr('fill', 'none')
			.attr('stroke', '#d0d0d0')
			.attr('stroke-width', 2)
			.appendTo($svg);

		}; // End initHexes

		// INIT HEXES - If the feature images are
		// on the page, turn into hexs
		if( $('.feature-imgs').length ) {
			initHexs();
		}

}); // End jQuery load


$(window).load(function() {

	/* ===================================================== */
	/*	Preloader
	/* ===================================================== */

	$('.preloader').fadeOut('slow');


	/* ===================================================== */
	/*  Animations
	/* ===================================================== */

	if (isMobile.any() === null) {
		$('.animated').appear(function() {
			var elem = $(this),
				delay = elem.data('animate-delay'),
				animation = elem.data('animate');
			if (delay === undefined) {
				delay = '0';
			}

			setTimeout(function() {
				elem.addClass(animation).css('visibility', 'visible');
				elem.addClass(animation).find('i').css('visibility', 'visible');
			}, delay);
		}); // End appear
	} else {
		$('.animated').css('visibility', 'visible').find('i').css('visibility', 'visible');
	}


	/* ==================================================== */
	/*	Skills
	/* ==================================================== */

	var showSkills = function() {
		$('.skill-bar').easyPieChart({
			easing: 'easeOutBounce',
			size: 140,
			animate: 2000,
			lineWidth: 6,
			lineCap: 'butt',
			barColor: '#bbb',
			trackColor: '#f0f0f0',
			scaleColor: false,
			rotate: 270
		});
	};

	//Animate skills if not a mobile device
	if (isMobile.any() === null) {
		$('.skill-semi').appear(function() {
			setTimeout(function() {
				showSkills();
			}, 1000);
		});
	// Else show straight away
	} else {
		showSkills();
	}


	/* ===================================================== */
	/*	Progress Bar
	/* ===================================================== */

	// For each progress bar, set original width.
	$(".progress").each(function() {
		$(this).data("origWidth", $(this).width()).width(0);
	});

	if (isMobile.any() === null) {
		$(".progress").appear(function() {
			$(this).each(function() {
				$(this).animate({
					width: $(this).data("origWidth")
				}, 2000);
			});
		});
	} else {
		$(".progress").each(function() {
			$(this).css('width', $(this).data("origWidth"));
		});
	}



	/* ==================================================== */
	/*	Google Maps											*/
	/* ==================================================== */


	var initGmap = function(latLng) {
		var mapInfo = $('#content').html();


		var mapStyles = [{
			"featureType": "landscape",
			"stylers": [{
				"saturation": -100
			}, {
				"lightness": 65
			}, {
				"visibility": "on"
			}]
		}, {
			"featureType": "poi",
			"stylers": [{
				"saturation": -100
			}, {
				"lightness": 51
			}, {
				"visibility": "simplified"
			}]
		}, {
			"featureType": "road.highway",
			"stylers": [{
				"saturation": -100
			}, {
				"visibility": "simplified"
			}]
		}, {
			"featureType": "road.arterial",
			"stylers": [{
				"saturation": -100
			}, {
				"lightness": 30
			}, {
				"visibility": "on"
			}]
		}, {
			"featureType": "road.local",
			"stylers": [{
				"saturation": -100
			}, {
				"lightness": 40
			}, {
				"visibility": "on"
			}]
		}, {
			"featureType": "transit",
			"stylers": [{
				"saturation": -100
			}, {
				"visibility": "simplified"
			}]
		}, {
			"featureType": "administrative.province",
			"stylers": [{
				"visibility": "off"
			}]
		}, {
			"featureType": "water",
			"elementType": "labels",
			"stylers": [{
				"visibility": "on"
			}, {
				"lightness": -25
			}, {
				"saturation": -100
			}]
		}, {
			"featureType": "water",
			"elementType": "geometry",
			"stylers": [{
				"hue": "#ffff00"
			}, {
				"lightness": -25
			}, {
				"saturation": -97
			}]
		}];

		var mapPos = new google.maps.LatLng(lat, lng); // Add the coordinates
		var mapOptions = {
			zoom: 16, // The initial zoom level when your map loads (0-20)
			center: mapPos, // Centre the Map to our coordinates variable
			mapTypeId: google.maps.MapTypeId.ROADMAP, // Set the type of Map
			styles: mapStyles,
			scrollwheel: false
		};

		var map = new google.maps.Map(document.getElementById('google-map'), mapOptions); // Render our map within the empty div

		// CUSTOM MARKER
		var markerImg = new google.maps.MarkerImage("img/marker.png", null, null, null, new google.maps.Size(40, 52));
		var marker = new google.maps.Marker({
			position: mapPos,
			icon: markerImg,
			map: map,
			title: 'Click to visit our company on Google Places'
		});

		// ADD INFOWINDOW
		var infowindow = new google.maps.InfoWindow({
			content: mapInfo
		});
		google.maps.event.addListener(marker, 'click', function() {
			infowindow.open(map, marker);
		});

		// MAP BUTTON
		$('.map-button').click(function(event) {
			event.preventDefault();
			if ($('.map-wrapper').height() === 0) {
				$('.map-wrapper').css('height', $('#google-map').height());
			} else {
				$('.map-wrapper').css('height', 0);
			}
		});

		// RESIZE MAP ON BROWER RESIZE
		google.maps.event.addDomListener(window, "resize", function() {
			var center = map.getCenter();
			google.maps.event.trigger(map, "resize");
			map.setCenter(center);
		});
	};

	// INIT MAP - if gmap div is present and api is loaded
	if( $('#google-map').length ) {
		initGmap(lat, lng);
	}


}); // End Windows Load


/* ===================================================== */
/*	Google custom search
/* ===================================================== */

(function() {
	if (search_id !== '') {
		var cx = search_id;
		var gcse = document.createElement('script');
		gcse.type = 'text/javascript';
		gcse.async = true;
		gcse.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') +
			'//www.google.com/cse/cse.js?cx=' + cx;
		var s = document.getElementsByTagName('script')[0];
		s.parentNode.insertBefore(gcse, s);
	}
})();

})(jQuery);