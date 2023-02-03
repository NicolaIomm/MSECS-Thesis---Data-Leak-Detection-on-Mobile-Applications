import frida, sys

def on_message(message, data):
	if (message['type'] == 'send'):
		print("[*] {0}".format(message['payload']))
	else:
		print(message)

jscode = """
Java.perform(() => {

	function bin2String(array) {
	  var result = "";
	  for (var i = 0; i < array.length; i++) {
	    result += String.fromCharCode(array[i]);
	  }
	  return result;
	}

	const Mac = Java.use('javax.crypto.Mac');
	const SecretKeySpec = Java.use('javax.crypto.spec.SecretKeySpec');

	const Mac_getInstance = Mac.getInstance.overload('java.lang.String');
	const Mac_init = Mac.init.overload('java.security.Key');
	const Mac_update = Mac.update.overload('[B');
	const SecretKeySpec_new = SecretKeySpec.$init.overload('[B', 'java.lang.String');

	Mac_getInstance.implementation = function (str) {
		const result = Mac_getInstance.call(this, str);
		console.log('[+] Entering Mac.getInstance()');
		console.log('[ ] algo: ' + str);
		console.log('[-] Leaving Mac.getInstance()');
		console.log('');
		return result;
	};

	Mac_init.implementation = function (key) {
		const result = Mac_init.call(this, key);
		console.log('[+] Entering Mac.init()');
		console.log('[ ] key: ' + bin2String(key.getEncoded()));
		console.log('[-] Leaving Mac.init()');
		console.log('');
		return result;
	};

	Mac_update.implementation = function (bytes) {
		const result = Mac_update.call(this, bytes);
		console.log('[+] Entering Mac.update()');
		console.log('[ ] bytes: ' + bin2String(bytes));
		console.log('[-] Leaving Mac.update()');
		console.log('');
		return result;
	}

	SecretKeySpec_new.implementation = function (bytes, str) {
		const result = SecretKeySpec_new.call(this, bytes, str);
		console.log('[+] Entering SecretKeySpec()');
		console.log('[ ] bytes: ' + bin2String(bytes));
		console.log('[ ] algo: ' + str);
		console.log('[-] Leaving SecretKeySpec()');
		console.log('');
		return result;
	}

});
"""

process = frida.get_usb_device().attach('cc.pacer.androidapp')
script = process.create_script(jscode)
script.on('message', on_message)
print('[*] Running cc.pacer.androidapp')
script.load()
sys.stdin.read()
