export const randomIban = () =>
  `NL${Math.floor(10 + Math.random() * 90)}BANK${Math.floor(1000000000 + Math.random() * 9000000000)}`;

const names = [
  "Jan van den Berg",
  "Pieter de Vries",
  "Martijn Jansen",
  "Sander Bakker",
  "Koen Visser",
  "Bram de Jong",
  "Rick van Dijk",
  "Lars Meijer",
  "Thijs Smit",
  "Daan de Boer",
  "Ruben Vermeer",
  "Bas van Dam",
  "Wouter Hermans",
  "Nick Peeters",
  "Tim Jacobs",
  "Jeroen van Leeuwen",
  "Tom Willems",
  "Jasper de Groot",
  "Floris de Bruin",
  "Stefan Kuipers",
  "Mark Bos",
  "Dennis van der Linden",
  "Sven Mulder",
  "Joost Hoekstra",
  "Niels Maas",
  "Luuk Schouten",
  "Robin van der Meer",
  "Casper Vos",
  "Sam van den Heuvel",
  "Timo van Loon",
  "Hugo de Lange",
  "Victor de Wit",
  "Gert-Jan van der Wal",
  "Maarten Koster",
  "Leon van Veen",
  "Stijn van Wijk",
  "Roy van den Broek",
  "Erik Post",
  "Bastiaan van der Ven",
  "Joep van Rossum",
  "Dirk van der Meulen",
  "Freek van Gils",
  "Michel van Hoorn",
  "Rik Timmermans",
  "Diederik van Rijn",
  "Arjen van der Zanden",
  "Tobias Blom",
  "Gerben Veenstra",
  "Koos de Graaf",
  "Frank van Beek",
];

export const randomName = () => names[Math.floor(Math.random() * names.length)];

export const randomAmount = () => Math.floor(Math.random() * 10000) + 15;

const descriptions = [
  "Huur maand maart",
  "Betaling factuur #2039",
  "Terugbetaling lening",
  "Supermarkt boodschappen",
  "Abonnement Spotify",
  "Energiekosten februari",
  "Overboeking naar spaarrekening",
  "Betaald aan Bol.com",
  "Cadeau voor verjaardag",
  "Teruggave te veel betaald",
  "Gemeentelijke belastingen",
  "Telefoonrekening KPN",
  "Lidmaatschap sportvereniging",
  "Tikkie terugbetaling",
  "Kinderopvangkosten",
  "Reiskosten NS",
  "Aankoop Marktplaats",
  "Zorgverzekering premie",
  "Studiegeld universiteit",
  "Benzinekosten Shell",
  "Restaurantdiner zaterdag",
  "Parkeerkosten Q-Park",
  "Waterrekening Vitens",
  "Autoverzekering premie",
  "Afrekening boodschappen",
  "Salaris uitbetaling",
  "Geld overgemaakt aan vriend",
  "Vakantiegeld storting",
  "Donatie aan goed doel",
  "Netflix maandelijkse betaling",
];

export const randomDescription = () =>
  descriptions[Math.floor(Math.random() * names.length)];

export type User = {
  username: string;
  name: string;
  iban: string;
};

export const users: User[] = [
  {
    username: "johnmdoe",
    name: "John M. Doe",
    iban: "NL12INGB000123456789",
  },
  {
    username: "peter1_green",
    name: "Peter Green",
    iban: "NL79ABNA9455762838",
  },
  {
    username: "janeDoe_12",
    name: "Jane Doe",
    iban: "NL11RABO9391947572",
  },
];
