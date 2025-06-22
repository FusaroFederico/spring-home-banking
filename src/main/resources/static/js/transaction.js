const token = localStorage.getItem("token");
if (!token) {
	alert("Devi effettuare il login!");
    window.location.href = "login.html";
}

document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("transactionForm");
  const message = document.getElementById("transactionMessage");

  // Gestione invio transazione
  form.addEventListener("submit", async (e) => {
    e.preventDefault();
    const iban = document.getElementById("iban").value;
    const amount = parseFloat(document.getElementById("amount").value);
    const description = document.getElementById("description").value;
    
    try {
		// recupera i dati del conto personale
		const accData = await fetch("/api/accounts/me", {
			method: "GET",
			headers: { 'Authorization': `Bearer ${token}` }
		});
		
		if(!accData.ok){
			throw new Error("Errore nel caricamento dati")
		}
		
		const account = await accData.json();
		
		// effettua la post 
      const res = await fetch("/api/transactions/create", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify({ senderIban: account.iban, receiverIban: iban, amount, description })
      });

      if (!res.ok) {
        const err = await res.text();
        throw new Error(err || "Errore transazione");
      }

      alert("Transazione effettuata con successo ✅");
      window.location.href = "dashboard.html";

    } catch (err) {
      message.innerText = "❌ " + err.message;
    }
  });
});
