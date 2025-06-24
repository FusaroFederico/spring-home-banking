const apiBase = '/api/auth';

async function loadUserData() {
  const userInfoDiv = document.getElementById('userInfo');
  const errorMsg = document.getElementById('errorMsg');
  const token = localStorage.getItem('token');
  // se il token non è presente, visualizza un messaggio di errore e chiede di effettuare il login 
  if(!token){
	document.getElementById('navbar').classList.add('d-none');
	errorMsg.innerText = "Accesso negato. Effettua il login!";
	document.getElementById('loadingIndicator').classList.add('d-none');
	document.getElementById('errorContainer').classList.remove('d-none');
  } else {
	    // recupera le informazione dell'utente tramite fetch
		await fetch('/api/users/me', {
	        method: 'GET',
	        headers: {
	          'Authorization': `Bearer ${token}`,
	          'Content-Type': 'application/json'
	        }
        })
        .then(response => {
		  // se la risposta non è ok lancia un'eccezione
          if (!response.ok) {
            if (response.status === 401 || response.status === 403) {
              throw new Error("Accesso negato. Effettua nuovamente il login.");
            } else {
              throw new Error("Errore imprevisto durante il caricamento.");
            }
          }
          return response.json();
        })
        .then(user => {
	        // inserisce i dati utente nell'html
	        document.getElementById('firstName').innerText = user.firstName;
	        document.getElementById('lastName').innerText = user.lastName;
	        document.getElementById('email').innerText = user.email;
	        userInfoDiv.classList.remove('d-none');
	        document.getElementById('loadingIndicator').classList.add('d-none');
	        // recupera le info sul conto
	        fetch('/api/accounts/me', {
				method: "GET",
      			headers: { 'Authorization': `Bearer ${token}` }
    			})
			.then(response => {
				if(!response.ok){
					if(response.status === 404){
						// Nessun conto esistente => bottone per crearne uno
					      document.getElementById('noAccountSection').classList.remove('d-none');
					      document.getElementById('createAccountBtn').addEventListener('click', createAccount);
					} else {
						throw new Error("Errore durante il caricamento dei dati.");
					}
				}
				
				return response.json();
			})
			.then(accData => {
				// inserisce i dati ricevuti nell'html
			  document.getElementById('iban').innerText = accData.iban;
		      document.getElementById('balance').innerText = accData.balance.toFixed(2);
		      document.getElementById('accountSection').classList.remove('d-none');
		      
		      // recupera i dati sulle transazioni
			  fetch('/api/transactions/account/' + accData.id, {
						method: "GET",
      					headers: { 'Authorization': `Bearer ${token}` }
    			})
    			.then(res => {
					if (!res.ok) {
						// se la risposta non è ok 
						if(res.status === 404) {
							document.getElementById('noTransactions').classList.remove('d-none');
						} else {
							throw new Error("Errore nel caricamento dello storico transizioni.");
						}
					}
					return res.json();
				})
				.then(txData => {
					if(txData.length === 0){
						// no transazioni => visualizza la sezione noTransactions
						document.getElementById('noTransactions').classList.remove('d-none');
					} else {
						const txHistory = document.getElementById('transactionHistory');
						txHistory.innerHTML = ` `;
						// per ogni transazione crea una riga e la aggiunge alla tabella
						txData.forEach(tx => {
							const row = document.createElement('tr');
							row.innerHTML = ` 
								  <td>${new Date(tx.timestamp).toLocaleString()}</td>
						          <td>${tx.senderIban}</td>
						          <td>${tx.receiverIban}</td>
						          <td>${tx.amount.toFixed(2)}</td>
	          					`;
	          				txHistory.appendChild(row);
						});
						document.getElementById('transactionContainer').classList.remove('d-none');
						
					}
				});
			});
	      })
	      .catch(error => {
			// in caso di errore visualizza un messaggio
			errorMsg.innerText = error.message;
			document.getElementById('loadingIndicator').classList.add('d-none');
			document.getElementById('errorContainer').classList.remove('d-none');
	    });
  }
  
}

// funzione logout => rimuove il token e reindirizza al login
function logout() {
  localStorage.removeItem('token');
  alert('Logout avvenuto con successo!')
  window.location.href = 'login.html';
}

// funzione per creare un conto
async function createAccount() {
    const token = localStorage.getItem('token');
	try {
		// invia la post all'endpoint preposto alla creazione di un conto
	    const res = await fetch('/api/accounts/create', {
	      method: 'POST',
	      headers: {
	        'Authorization': `Bearer ${token}`
	      }
	    });
	
	    if (!res.ok) throw new Error('Errore nella creazione del conto');
		// in caso di risposta positiva lancia un alert e ricarica la pagina
	    alert('Conto creato con successo!');
	    window.location.reload();

    } catch (err) {
		// in caso di errore lancia un alert
	    console.error(err);
	    alert('Non è stato possibile creare il conto.');
    }
}

// Espone le funzioni nel contesto globale (per i pulsanti onclick)
window.logout = logout;
window.loadUserData = loadUserData;
window.createAccount = createAccount;

document.addEventListener('DOMContentLoaded', () => {

  const loginForm = document.getElementById('loginForm');
  const registerForm = document.getElementById('registerForm');
  // sezione login
  if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const email = document.getElementById('loginEmail').value;
      const password = document.getElementById('loginPassword').value;
	  // prende i dati dal form e li invia al backend per l'autenticazione
      try {
        const res = await fetch(`${apiBase}/login`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ email, password })
        });
		// se la risposta non è ok lancia un errore
        if (!res.ok) throw new Error('Login fallito!');
        // salva il token in localStorage e indirizza alla dashboard
        const data = await res.json();
        localStorage.setItem('token', data.token);
        window.location.href = 'dashboard.html'
      } catch (err) {
		// in caso di errore visualizza un messaggio
        document.getElementById('loginError').innerText = err.message;
      }
    });
  }
  // sezione registrazione
  if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      // crea un json con i dati del form
      const body = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('registerEmail').value,
        password: document.getElementById('registerPassword').value,
      };
	  
      try {
		// invia la richiesta di registrazione al backend
        const res = await fetch(`${apiBase}/register`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body)
        });
		// se la risposta non è ok lancia un errore
        if (!res.ok) throw new Error('Registrazione fallita');
        // altrimenti reindirizza al login
        alert('Accoun creato, ora puoi effettuare il login.');
        window.location.href = 'login.html';
      } catch (err) {
		// in  caso di errore visualizza un messaggio
        document.getElementById('registerError').innerText = err.message;
      }
    });
  }

  // Carica i dati utente solo se siamo in dashboard
  if (document.getElementById('userInfo')) {
    loadUserData();
  }
});
