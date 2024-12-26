const POST_URL = "http://localhost:8080/user"

async function createUser() {
    clearErrorMessages();

    const userTypeInput = document.querySelector('input[name="userType"]:checked').value;

    // Validate user first name lenght
    const userFirstName = String(document.getElementById("userFirstNameInput").value.trim() || "");
    if (userFirstName.length < 3 || userFirstName.length > 20) {
        const firstNameError = document.getElementById("firstNameError");

        firstNameError.textContent = "The first name must have 3 to 20 caracters long.";
        firstNameError.style.display = "block";
        return false;
    }

    // Validate user last name lenght
    const userLastName = String(document.getElementById("userLastNameInput").value.trim() || "");
    if (userLastName.length < 3 || userLastName.length > 20) {
        const lastNameError = document.getElementById("lastNameError");

        lastNameError.textContent = "The last name must have 3 to 20 caracters long.";
        lastNameError.style.display = "block";
        return false;
    }

    // Validate user document
    const userDocument = String(document.getElementById("userDocumentInput").value.trim().replace(/\D/g, '') || "");
    if (userTypeInput === "COMMON" && (userDocument.length !== 11 || !validateCPF(userDocument))) {
        const documentError = document.getElementById("documentError");

        documentError.textContent = "Enter a valid CPF (e.g., 123.456.789-09).";
        documentError.style.display = "block";
        return false;

    } else if (userTypeInput === "MERCHANT" && (userDocument.length !== 14 || !validateCNPJ(userDocument))) {
        const documentError = document.getElementById("documentError");

        documentError.textContent = "Enter a valid CNPJ (e.g., 12.345.678/0001-95).";
        documentError.style.display = "block";
        return false;
    }

    // Validate user email
    const userEmail = String(document.getElementById("userEmailInput").value.trim() || "/");
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(userEmail)) {
        const emailError = document.getElementById("emailError");

        emailError.textContent = "Enter a valid email address (e.g., user@example.com)."
        emailError.style.display = "block";
        return false;
    }

    // Validate user password
    const userPassword = String(document.getElementById("userPasswordInput").value.trim() || "");
    if (userPassword.length < 6 || userPassword > 60) {
        const passwordError = document.getElementById("passwordError");

        passwordError.textContent = "The password must have 6 to 60 caracters long."
        passwordError.style.display = "block";
        return false;
    }

    const userBalance = document.getElementById("userBalanceInput").value.trim();


    body = {
        "firstName": userFirstName,
        "lastName": userLastName,
        "document": userDocument,
        "email": userEmail,
        "password": userPassword,
        "balance": userBalance,
        "userType": userTypeInput
    }

    createPost(body)
}

async function createPost(body) {
    const response = await fetch(POST_URL, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    if (response.ok) {
        const responseData = await response.json();
        alert("User created successfully!");
        console.log(responseData);
    } else {
        errorData = await response.json();
        console.log(`Error: ${errorData.message}\nStatus: ${errorData.statusCode}.`)
    }
}

function clearErrorMessages() {
    const errorMessages = document.querySelectorAll('.error-message');
    errorMessages.forEach(error => {
        error.textContent = '';
        error.style.display = 'none';
    });
}

function validateCPF(cpf) {
    const cpfRegex = /^\d{11}$/;
    return cpfRegex.test(cpf);
}

function validateCNPJ(cnpj) {
    const cnpjRegex = /^\d{14}$/;
    return cnpjRegex.test(cnpj);
}

document.getElementById("userDocumentInput").addEventListener("input", function (event) {
    const userType = document.querySelector('input[name="userType"]:checked').value;
    let input = event.target;
    let value = input.value.replace(/\D/g, '');  // Remove non-numeric digits

    // Apply mask for CPF (11 digits) or CNPJ (14 digits)
    if (userType === "COMMON" && value.length === 11) {
        value = value.replace(/(\d{3})(\d{3})(\d{3})(\d{1})/, '$1.$2.$3-$4');
    } else if (userType === "MERCHANT" && value.length === 14) {
        value = value.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, '$1.$2.$3/$4-$5');
    }

    // Set the masked value in the input field
    input.value = value;
});