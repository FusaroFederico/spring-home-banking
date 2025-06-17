const apiBase = '/api/auth';

console.log("Script caricato");

async function loadUserData() {
  const contentDiv = document.getElementById('content');
  const token = localStorage.getItem('token');
  console.log("Token salvato:", token);

  if(!token){
	contentDiv.innerHTML = `<p class="error">Accesso negato. Effettua il login.</p>
							<a href="login.html"><button> Login </button></a>`;
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
	        // costruisce html con i dati utente e li inserisce nel div
	        const userDiv = document.createElement('div');
	        userDiv.className = 'user-info';
	        userDiv.innerHTML = `
	          <h2>Benvenuto, ${user.firstName}!</h2>
	          <p><strong>Nome:</strong> ${user.firstName}</p>
	          <p><strong>Cognome:</strong> ${user.lastName}</p>
	          <p><strong>Email:</strong> ${user.email}</p>
	          <button onclick="logout()">Logout</button>
	        `;
	        contentDiv.innerHTML = '';
	        contentDiv.appendChild(userDiv);
	        // recupera le info sul conto
	        fetch('/api/accounts/me', {
				method: "GET",
      			headers: { 'Authorization': `Bearer ${token}` }
    			})
			.then(response => {
				if(!response.ok){
					if(response.status === 404){
						// Nessun conto esistente => bottone per crearne uno
					      document.getElementById('noAccountSection').style.display = 'block';
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
		      document.getElementById('accountSection').style.display = 'block';
			});
	      })
	      .catch(error => {
	        contentDiv.innerHTML = `<p class="error">${error.message}</p>`;
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

        if (!res.ok) throw new Error('Registration failed');
        alert('Account created, you can now log in.');
        window.location.href = 'login.html';
      } catch (err) {
        document.getElementById('registerError').innerText = err.message;
      }
    });
  }

  // Carica i dati utente solo se siamo in dashboard
  if (document.getElementById('content')) {
    loadUserData();
  }
});
