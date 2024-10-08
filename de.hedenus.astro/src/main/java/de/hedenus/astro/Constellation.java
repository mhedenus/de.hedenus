package de.hedenus.astro;

public enum Constellation
{
	And("Andromeda"), //
	Ant("Antlia"), //
	Aps("Apus"), //
	Aql("Aquila"), //
	Aqr("Aquarius"), //
	Ara("Ara"), //
	Ari("Aries"), //
	Aur("Auriga"), //
	Boo("Boötes"), //
	Cae("Caelum"), //
	Cam("Camelopardalis"), //
	Cap("Capricornus"), //
	Car("Carina"), //
	Cas("Cassiopeia"), //
	Cen("Centaurus"), //
	Cep("Cepheus"), //
	Cet("Cetus"), //
	Cha("Chamaeleon"), //
	Cir("Circinus"), //
	CMa("Canis Major"), //
	CMi("Canis Minor"), //
	Cnc("Cancer"), //
	Col("Columba"), //
	Com("Coma Berenices"), //
	CrA("Corona Australis"), //
	CrB("Corona Borealis"), //
	Crt("Crater"), //
	Cru("Crux"), //
	Crv("Corvus"), //
	CVn("Canes Venatici"), //
	Cyg("Cygnus"), //
	Del("Delphinus"), //
	Dor("Dorado"), //
	Dra("Draco"), //
	Equ("Equuleus"), //
	Eri("Eridanus"), //
	For("Fornax"), //
	Gem("Gemini"), //
	Gru("Grus"), //
	Her("Hercules"), //
	Hor("Horologium"), //
	Hya("Hydra"), //
	Hyi("Hydrus"), //
	Ind("Indus"), //
	Lac("Lacerta"), //
	Leo("Leo"), //
	Lep("Lepus"), //
	Lib("Libra"), //
	LMi("Leo Minor"), //
	Lup("Lupus"), //
	Lyn("Lynx"), //
	Lyr("Lyra"), //
	Men("Mensa"), //
	Mic("Microscopium"), //
	Mon("Monoceros"), //
	Mus("Musca"), //
	Nor("Norma"), //
	Oct("Octans"), //
	Oph("Ophiuchus"), //
	Ori("Orion"), //
	Pav("Pavo"), //
	Peg("Pegasus"), //
	Per("Perseus"), //
	Phe("Phoenix"), //
	Pic("Pictor"), //
	PsA("Piscis Austrinus"), //
	Psc("Pisces"), //
	Pup("Puppis"), //
	Pyx("Pyxis"), //
	Ret("Reticulum"), //
	Scl("Sculptor"), //
	Sco("Scorpius"), //
	Sct("Scutum"), //
	Ser("Serpens"), //
	Ser1("Serpens Caput"), //
	Ser2("Serpens Cauda"), //
	Sex("Sextans"), //
	Sge("Sagitta"), //
	Sgr("Sagittarius"), //
	Tau("Taurus"), //
	Tel("Telescopium"), //
	TrA("Triangulum Australe"), //
	Tri("Triangulum"), //
	Tuc("Tucana"), //
	UMa("Ursa Major"), //
	UMi("Ursa Minor"), //
	Vel("Vela"), //
	Vir("Virgo"), //
	Vol("Volans"), //
	Vul("Vulpecula");

	private final String IAU_name;

	private Constellation(final String IAU_name)
	{
		this.IAU_name = IAU_name;
	}

	public String IAU_name()
	{
		return IAU_name;
	}

	public String label()
	{
		return switch (this)
		{
			case Ser1 -> Ser.name() + "\u00b9";
			case Ser2 -> Ser.name() + "²";
			default -> name();
		};
	}
}