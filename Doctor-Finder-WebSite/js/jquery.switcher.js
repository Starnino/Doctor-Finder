// style tag needs an ID of #colors
(function($) {
	$.fn.switcher = function() {
		var Switcher = new Switch(this);
		Switcher.hidePanel();

		Switcher.createButtons();
	};

	var Switch = function(panel) {
			this.switcherPanel = panel;
		};

	Switch.prototype = {

		calcWidth: function() {
			this.switcherWidth = -this.switcherPanel.find('.content').outerWidth() -4;
		},

		hidePanel: function() {
			this.calcWidth();
			this.isHidden = true;
			this.switcherPanel.animate({
				left: this.switcherWidth
			}, 2000);
		},

		createButtons: function() {
			$("#switcher .btn").click({
				button: this
			}, this.switcherButton);
			$("#layout-button").click({
				switcherPanel: this.switcherPanel
			}, this.layoutToggle);
			$("#background-colors li a").click({
				switcherPanel: this.switcherPanel
			}, this.bgColorBtn);
			$("#theme-colors li a").click({
				switcherPanel: this.switcherPanel
			}, this.themeColorBtns);
			$("#background-images li a").click({
				switcherPanel: this.switcherPanel
			}, this.bgImgBtns);
			$("#header-switcher li a").click({
				switcherPanel: this.switcherPanel
			}, this.headerBtns);
		},

		switcherButton: function(event) {
			// Toggle button to show/hide switcher panel
			var myParent = event.data.button;
			if(myParent.isHidden === true) { // Extend
				myParent.isHidden = false;
				myParent.switcherPanel.animate({
					left: '0'
				});
			} else { // Retract
				myParent.isHidden = true;
				myParent.switcherPanel.animate({
					left: myParent.switcherWidth
				});
			}
			myParent.switcherPanel.addClass('out');
			return false;
		},

		layoutToggle: function() {
			// Button to toggle boxed/stretched layout
			if(!$('#wrap').hasClass('boxed')) {
				$('#wrap').addClass('boxed');
			} else {
				$('#wrap').removeClass('boxed');
			}
			var hasRun = true;
			$(window).resize();
			return false;
		},

		bgColorBtn: function(e) {
			// Cycle list of background colors and assign click function to change background color
			var color = $(this).attr('class');
			$("body").css("background", color);
			e.preventDefault();
		},

		themeColorBtns: function() {
			// $('.logo img').attr({ src: 'img/logo-gray.png'});
			color = $(this).attr('class').split(' ')[0]; // Gets color from clicked button
			if( $('#colors').length === 0 ) {
				$('head').append('<link id="colors" rel="stylesheet" href="css/colors/style-' + color + '.css">');
			} else {
				$("#colors").attr("href", "css/colors/style-" + color + ".css"); // Attaches corrisponding stylesheet
			}
			return false;
		},

		bgImgBtns: function(e) {
			// Cycle list of background images and assign click function to change background image
			if(!$('#wrap').hasClass('boxed')) {
				$('#wrap').addClass('boxed');
			}
			var color = $(this).attr('class');
			var image = $(this).css('backgroundImage');
			$("body").css("backgroundImage", image);
			e.preventDefault();
		},

		headerBtns: function(e) {
			var color = $(this).css('background-color');
	
			if($(this).attr('id') == 'header-color') {
				$('#header').css("background-color", "");
			} else {
				$('#header').css('background-color', color );
			}

			e.preventDefault();

		}
	};
})(jQuery);