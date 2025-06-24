const token = localStorage.getItem("token");
if (!token) window.location.href = "login.html";

document.addEventListener("DOMContentLoaded", () => {
  const createForm = document.getElementById("createCardForm");
  const createMessage = document.getElementById("createCardMessage");

  // se è presente il form
  if(createForm){
	  createForm.addEventListener("submit", async (e) => {
	    e.preventDefault();
	    const type = document.getElementById("cardType").value;
	    // effettua la chiamata post 
	    try {
	      const res = await fetch(`/api/cards/create?type=${type}`, {
	        method: "POST",
	        headers: { Authorization: `Bearer ${token}` }
	      });
	      // se la risposta non è ok lancia un errore
	      if (!res.ok) throw new Error("Errore creazione carta");
	      // se è ok espone un alert con esito positivo e indirizza alla dashboard
	      alert("Carta creata con successo ✅")
	      window.location.href = "dashboard.html"
	    } catch (err) {
	      createMessage.textContent = err.message;
	    }
	  });
  } else {
	// altrimenti carica i dati sulle carte
	loadCards();
  }
});

// funzione che carica i dati sulle carte
async function loadCards() {
	const cardList = document.getElementById("cards-list");
    try {
		  // effettua la chiamata GET 
	      const res = await fetch("/api/cards/mycards", {
	        headers: { Authorization: `Bearer ${token}` }
	      });
	      // converte la risposta in json
	      const cards = await res.json();
	      if(cards.length === 0){
			// se la lunghezza è 0 => non ci sono carte
			  document.getElementById('no-cards-msg').classList.remove('d-none');
		  } else {
		      cardList.innerHTML = "";
		      // per ogni carta genere una riga nella tabella carte
		      cards.forEach(card => {
		        const row = document.createElement("tr");
		        row.innerHTML = `
		          <td>${card.cardNumber}</td>
		          <td>${card.type === "DEBIT" ? "DEBITO" : "PREPAGATA"}</td>
		          <td>${card.expirationDate}</td>
		          <td>${card.balance != null ? '€ ' + card.balance.toFixed(2) : '-'}</td>
		          <td>
		            ${card.type === "PREPAID" ? `
		            <form onsubmit="topUpCard(event, ${card.cardNumber})" class="d-flex gap-2">
		              <input type="number" class="form-control form-control-sm" min="1" step="0.01" placeholder="Importo" required />
		              <button type="submit" class="btn btn-sm btn-primary">Ricarica</button>
		            </form>
		            ` : '-'}
		          </td>`;
		        cardList.appendChild(row);
		      });
		      document.getElementById('cards-container').classList.remove('d-none');
		  }
    } catch (err) {
		// in caso di errore, visualizza una tabella vuota con un messaggio
      	  cardList.innerHTML = '<tr><td colspan="5">Errore caricamento carte</td></tr>';
      	  document.getElementById('cards-container').classList.remove('d-none');
    }
}

// metodo per ricaricare una carta prepagata
async function topUpCard(event, cardNumber) {
  event.preventDefault();
  const amountInput = event.target.querySelector("input");
  const amount = parseFloat(amountInput.value);
  try {
    // inoltra la chiamata post all'endpoint 
    const res = await fetch("/api/cards/topup", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`
      },
      body: JSON.stringify({ cardNumber, amount })
    });
	// se la risposta non è ok, lancia un errore con messaggio = body della res
    if (!res.ok) {
		const errorBody = await res.text();
		throw new Error(errorBody || "Errore imprevisto.");
	} 
	// altrimenti resetta l'importo, visualizza un alert e ricarica la pagina
    amountInput.value = "";
    alert("Ricarica effettuata con successo ✅");
    location.reload();
  } catch (err) {
    alert(err.message);
  }
}
