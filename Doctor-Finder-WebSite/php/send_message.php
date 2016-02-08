<?php

if(isset($_POST['email'])) { // Check if the form was submitted else redirect to home page.
	require_once 'config.php';

	$message = '';
	$error = '';
	$alert = '';

	$to = 'info@doctorfinderapp.com';

	$name = strip_tags($_POST['name']);
	$message = strip_tags($_POST['message']);
	$email = $_POST['email'];
	if(isset($_POST['subject'])) {
		$subject = strip_tags($_POST['subject']);
	}

	if(strlen($name)<2) {
		$errors[] = 'Inserisci il tuo nome.';
	}

	if(strlen($message)<1) {
		$errors[] = 'Inserisci una breve descrizione.';
	} elseif(strlen($message)<20) {
		$errors[] = 'La descrizione deve contenere almeno 20 caratteri.';
	}

	if(!filter_var($email, FILTER_VALIDATE_EMAIL)) {
		$errors[] = 'Inserisci un indirizzo email valido.';	
	}

	$body = "From: $name \n \n";

	if(isset($subject)) {
		$subject_pre = "Subject: ";
		if(strlen($subject)<1) {
			$subject = $subject_pre.'None';
		} else {
			$subject = $subject_pre.$subject;
		}
		$body .= $subject." \n \n";
	} else {
		$subject = '';
	}

	$body .= $message;
	$headers = "From: $email";

	if(mail($to, $subject, $body, $headers)) {
		$alert = 'Grazie. A breve verrai contattato dal nostro staff.';
	} else {
		$errors[] = 'Errore. Prova a reinviare il messaggio. ';
	}

	if(isset($errors) && count($errors) > 0) {
		$html = "<div class='contact-error alert error'>";
		foreach($errors as $error) {
			$html .="$error<br>";
		}
		$html .= '</div>';
	} else {
		$html = "<div class='alert success'>";
		$html .= $alert;
		$html .= "</div>";
		
	}

	echo $html;
} else {
	header('Location: ../index.html'); 
}
?>
