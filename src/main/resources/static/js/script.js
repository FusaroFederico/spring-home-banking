const apiBase = '/api/auth';

console.log("Script caricato");

async function loadUserData() {
  const userInfoDiv = document.getElementById('userInfo');
  const errorMsg = document.getElementById('errorMsg');
  const token = localStorage.getItem('token');
  console.log("Token salvato:", token);

  if(!token){
	document.getElementById('navbar').classList.add('d-none');
	errorMsg.innerText = "Accesso negato. Effettua il login!";
	document.getElementById('loadingIndicator').classList.add('d-none');
	document.getElementById('errorContainer').classList.remove('d-none');
  } else {
	  console.log("Invio fetch a /api/users/me con headers:", {
	    Authorization: `Bearer ${token}`
	  });
	  
		await fetch('/api/users/me', {
	        method: 'GET',
	        headers: {
	          'Authorization': `Bearer ${token}`,
	          'Content-Type': 'application/json'
	        }
        })
        .then(response => {
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
						document.getElementById('noTransactions').classList.remove('d-none');
					} else {
						const txHistory = document.getElementById('transactionHistory');
						txHistory.innerHTML = ` `;
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
			errorMsg.innerText = error.message;
			document.getElementById('loadingIndicator').classList.add('d-none');
			document.getElementById('errorContainer').classList.remove('d-none');
	    });
  }
  
}

function logout() {
  localStorage.removeItem('token');
  alert('Logout avvenuto con successo!')
  window.location.href = 'login.html';
}

async function createAccount() {
  const token = localStorage.getItem('token');

  try {
    const res = await fetch('/api/accounts/create', {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    });

    if (!res.ok) throw new Error('Failed to create account');

    alert('Conto creato con successo!');
    window.location.reload();

  } catch (err) {
    console.error(err);
    alert('Non è stato possibile creare il conto.');
  }
}

// Espone le funzioni nel contesto globale (per i pulsanti onclick)
window.logout = logout;
window.loadUserData = loadUserData;
window.createAccount = createAccount;

document.addEventListener('DOMContentLoaded', () => {
  console.log("📦 DOM completamente caricato");

  const loginForm = document.getElementById('loginForm');
  const registerForm = document.getElementById('registerForm');

  if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const email = document.getElementById('loginEmail').value;
      const password = document.getElementById('loginPassword').value;

      try {
        const res = await fetch(`${apiBase}/login`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ email, password })
        });

        if (!res.ok) throw new Error('Login failed');
        const data = await res.json();
        localStorage.setItem('token', data.token);
        window.location.href = 'dashboard.html'
      } catch (err) {
        document.getElementById('loginError').innerText = err.message;
      }
    });
  }

  if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
      e.preventDefault();
      const body = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('registerEmail').value,
        password: document.getElementById('registerPassword').value,
      };

      try {
        const res = await fetch(`${apiBase}/register`, {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(body)
        });

        if (!res.ok) throw new Error('Registrazione fallita');
        alert('Accoun creato, ora puoi effettuare il login.');
        window.location.href = 'login.html';
      } catch (err) {
        document.getElementById('registerError').innerText = err.message;
      }
    });
  }

  // Carica i dati utente solo se siamo in dashboard
  if (document.getElementById('userInfo')) {
    loadUserData();
  }
});
