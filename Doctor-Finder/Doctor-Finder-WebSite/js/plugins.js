// Avoid `console` errors in browsers that lack a console.
(function() {
    "use strict";

    var method;
    var noop = function() {};
    var methods = [
        'assert', 'clear', 'count', 'debug', 'dir', 'dirxml', 'error',
        'exception', 'group', 'groupCollapsed', 'groupEnd', 'info', 'log',
        'markTimeline', 'profile', 'profileEnd', 'table', 'time', 'timeEnd',
        'timeStamp', 'trace', 'warn'
    ];
    var length = methods.length;
    var console = (window.console = window.console || {});

    while (length--) {
        method = methods[length];

        // Only stub undefined methods.
        if (!console[method]) {
            console[method] = noop;
        }
    }
}());

/* ===================================================== */
/* Smooth Scrolling                                      */
/* ===================================================== */
(function($) {
    "use strict";

    $.smooth = function(elem, offset, func) {
        var pos;

        offset = typeof offset !== 'undefined' ? offset : 0;
        func = typeof func !== 'undefined' ? func : function() {};

        if ($.isNumeric(elem)) {
            pos = elem;
        } else {
            pos = $(elem).offset().top;
        }

        $('html, body').animate({
            scrollTop: pos + offset
        }, 2000, function() {
            func();
        });
    };

})(jQuery);

/* ===================================================== */
/* Contact Form
/* ===================================================== */

(function($) {
    "use strict";

    $.fn.contactValidation = function(options) {
        $(this).each(function() {
            var defaults = {
                inlineErrors: true,
                formName: $(this)
            },
                options_set = $.extend(defaults, options),
                ContactValidation = new Validation(options_set);
        });
    };

    var Validation = function(options) {
        var formName = options.formName;
        // Config
        var nameErrorMsg = 'Please leave your name',
            emailErrorMsg = 'Please provide a valid email address',
            longerErrorMsg = 'Please leave a longer message';

        // On focus out set error if empty
        var nameVal = (function() {
            $(this).unbind('keyup');
            if (!$(this).val()) {
                $(this).addClass("error");
                // Add error if not there already
                if ($(this).parent().find(".error-name").length === 0 && options.inlineErrors) {
                    $(this).parent().prepend('<span class="inline-error error-name">' + nameErrorMsg + '</span>');
                }
                $(this).keyup(function() {
                    $(this).triggerHandler('focusout');
                });
            } else {
                // Validates so remove errors and keyup trigger
                $(this).removeClass("error");
                $(this).parent().children('span').remove('.error-name');
            }
        });

        // On focus off set error if shorter than 5
        // characters
        var messageVal = (function() {
            $(this).unbind('keyup');
            if ($(this).val().length < 5) {
                $(this).addClass("error");
                if ($(this).parent().find(".error-message").length === 0 && options.inlineErrors) {
                    $(this).parent().prepend('<span class="inline-error error-message">' + longerErrorMsg + '</span>');
                }
                $(this).keyup(function() {
                    $(this).triggerHandler('focusout');
                });
            } else {
                // Validates so remove errors
                $(this).parent().children('span').remove('.error-message');
                $(this).removeClass("error");

            }
        });

        // On focus out set error if is not a valid email
        var emailVal = (function() {
            $(this).unbind('keyup');
            if ( !IsEmail( $(this).val() ) ) {
                $(this).addClass("error");
                if ($(this).parent().find(".error-email").length === 0 && options.inlineErrors) {
                    $(this).parent().prepend('<span class="inline-error error-email">' + emailErrorMsg + '</span>');
                }
                $(this).keyup(function() {
                    $(this).triggerHandler('focusout');
                });
            } else {
                // Validates so remove errors
                $(this).parent().children('span').remove('.error-email');
                $(this).removeClass("error");
                $(this).unbind('keyup');
            }
        });


        // Validate Email
        function IsEmail(email) {
            var regex = /^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
            return regex.test(email);
        }

        formName.submit(function() {
            // Trigger focusout on form submit to check for errors
            $(this).find('#message').focusout(messageVal);
            $(this).find('#email').focusout(emailVal);
            $(this).find('#name').focusout(nameVal);

            $(this).find('#message').triggerHandler("focusout");
            $(this).find('#email').triggerHandler("focusout");
            $(this).find('#name').triggerHandler("focusout");


            // If no error send to sendForm() for php submitting
            if (!$('input').hasClass('error') && !$('textarea').hasClass('error')) {
                sendForm();
            }
            return false;
        });

        var formWrapper = formName.parent();
        var speed = 100; // Speed of slide animation

        // Send form to PHP
        function sendForm() {

            // Add spinner and disable button
            formName.find('#submit').attr('disabled', true);
            formName.find('#submit').prepend('<i class="fa fa-spinner spin"></i>');
            formName.find('#submit i').css({
                'margin-right': '5px'
            });

            var formData = formName.serialize(); // Serialise form data to send
            $('.contact-error').slideUp(speed); // Hide previous attempt error message
            $.ajax({
                type: "POST",
                url: "php/send_message.php",
                data: formData,
                success: function(html) {
                    var alertBox = $(html).filter('.alert.error'); // Get alert box if alerady one
                    if (alertBox.length === 0) {
                        // If no alert box already, add new
                        html = formWrapper.append(html);
                        $('.contact-success').css('display', 'none').slideDown();
                        formName.slideUp(1000);
                    } else { // If exists add to it.
                        // If already a error displayed add to existing div
                        // Else add new div
                        if ($('.contact-error').length !== 0) {
                            $('.contact-error').html(alertBox.html());
                            $('.contact-error').slideDown(speed);
                        } else {
                            $(formName).before(html);
                            $('.contact-error').css('display', 'none');
                            $('.contact-error').slideDown(speed);
                        }
                    }

                    // Remove spinner and hide form
                    formName.find('.spin').remove();
                    formName.find('#submit').attr('disabled', false);
                }
            });
            return false;
        } // End SendForm fnc

    }; // End Validation fnc
})(jQuery);


/* ===================================================== */
/*  Alerts Plugin
/* ===================================================== */

(function($) {
    "use strict";

    $.fn.alerts = function() {
        var button = this;

        button.click(function() {
            $(this).parent().animate({
                opacity: "toggle"
            });
            return false;
        });

    };
})(jQuery);


/* ===================================================== */
/* Toggle Plugin
/* ===================================================== */

(function($) {
    "use strict";

    $.fn.toggle = function() {
        $(this).each(function() {
            var createToggles = new Toggle($(this));
        });
    };

    var Toggle = function(accordian) {
        var toggleContent = accordian.find('.content'),
            btnWrap = accordian.find('.trigger');

        var fullWidth = toggleContent.innerWidth(true);

        btnWrap.wrapInner("<a href='#' />");
        var btn = btnWrap.find('a');
        btn.append('<span>+</span>');
        toggleContent.css('width', fullWidth).hide().first().show();
        btn.first().toggleClass('current');

        // Remove focus after click, keyboard accessibility uneffected
        btn.mouseup(function() {
            $(this).blur();
        });

        // Styling for ipad hover
        btn.on('touchstart', function() {
            $(this).addClass('touch-hover');
        });

        btn.click(function(e) {
            if (e.which == 13) {}
            $(this).toggleClass('current').parent().next().slideToggle('normal');
            return false;
        });
    };
})(jQuery);


/* ===================================================== */
/* Tabs Pluin
/* ===================================================== */

(function($) {
  "use strict";

    $.fn.tabs = function(options) {
        $(this).each(function() {
            var CreateTabs = new Tabs($(this), options);
        });
    };

    var Tabs = function(container, options) {
        var contents = $(container.find('.content'));
        var btnWrap = $(container.find('.trigger'));
        this.container = container;

        container.prepend(btnWrap);

        if (btnWrap.find("a").length < 1) {
            btnWrap.wrapInner("<a href='#' />");
        }

        if (options.type === 'side') {
            btnWrap.wrapAll("<div class='tabs-wrap'></div>");
            contents.wrapAll("<div class='contents-wrap'></div>");
        }

        var btn = btnWrap.find('a');

        btnWrap.last().css('margin-right', '0');
        btn.first().addClass('current');

        // Hide content, show first
        contents.hide().first().show();
        btn.data('container', container);

        // Remove focus after click, keyboard accessibility uneffected
        btn.mouseup(function() {
            $(this).blur();
        });

        btn.click(function() {
            var container = $(this).data('container');
            container = $(container);
            var tabNum = $(this).parent().index();
            container.find('a').removeClass('current');
            $(this).addClass('current');
            container.css('minHeight', container.height());
            container.find('.content').fadeOut().hide();
            container.find('.tab' + tabNum).fadeIn(function() {
                container.css('minHeight', '0');
            });
            // container.css( 'height', 'auto' );
            return false;
        });

        return this;
    };

})(jQuery);


/* ===================================================== */
/* Accordian Plugin
/* ===================================================== */

(function($) {
  "use strict";

    $.fn.accordian = function() {
        $(this).each(function() {
            var CreateAccordian = new Accordian($(this));
        });
    };

    var Accordian = function(accordian) {
        var btnWrap = accordian.find('.trigger'),
            content = accordian.find('.content'),
            button;
        var fullWidth = content.outerWidth(true);
        btnWrap.wrapInner("<a href='#' />");
        button = btnWrap.find('a');
        button.append('<span>+</span>');
        content.hide().first().show();
        button.first().toggleClass('current');

        button.css({
            cursor: 'pointer'
        });

        button.mouseup(function() {
            $(this).blur();
        });

        // Styling for ipad hover
        button.on('touchstart', function() {
            $(this).addClass('touch-hover');
        });

        button.click(function() {
            $(this).parent().siblings().find('a').removeClass('current');
            $(this).parent().siblings('.content').slideUp('normal');
            $(this).addClass('current').parent().next().slideDown('normal');
            return false;
        });
        return this;
    };
})(jQuery);